/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 Eric Afenyo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ericafenyo.tracker.analysis

import android.content.Context
import androidx.annotation.WorkerThread
import com.ericafenyo.bikediary.logger.Logger
import com.ericafenyo.bikediary.model.Metrics
import com.ericafenyo.tracker.database.analyzed.AnalyzedData
import com.ericafenyo.tracker.database.analyzed.AnalyzedDataCache
import com.ericafenyo.tracker.database.analyzed.SensorLocation
import com.ericafenyo.tracker.database.analyzed.Trace
import com.ericafenyo.tracker.database.record.Record
import com.ericafenyo.tracker.database.record.RecordCache
import com.ericafenyo.tracker.location.SimpleLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

/**
 * This class contain methods for reading raw gps data and converting them into a GeoJson data.
 * Our gps data is stored in a local SQLite database as a list of [SimpleLocation] with some metadata.
 *
 * @see getCoordinate
 *
 * @author Eric
 *
 * created on 2021-01-25
 */
@Singleton
class Analyser @Inject constructor(@ApplicationContext val context: Context) {
  // Minimum points that defines a valid adventure.
  private val minimumPoints = 5

  @WorkerThread
  suspend fun startAnalysis(): Boolean {
    val analyzedData = analyzeData()
    if (analyzedData != null) {
      AnalyzedDataCache.getInstance(context).put(analyzedData)
    }
    return true
  }

  suspend fun getAnalysedAdventures(): List<AnalyzedData> {
    return AnalyzedDataCache.getInstance(context).entries()
  }

  private suspend fun analyzeData(): AnalyzedData? {
    val records = loadRecords()

    // Exit early if records are less than 5
    if (records.size < minimumPoints) {
      Logger.debug(context, TAG, "Invalid record list, skipping")
      return null
    }

    runCatching {
      val metrics = buildMetrics(records)

      val result = AnalyzedData(
        uuid = UUID.randomUUID().toString(),
        calories = metrics.calories,
        distance = metrics.distance,
        duration = metrics.duration,
        startTime = metrics.startedAt,
        endTime = metrics.completedAt,
        speed = metrics.speed,
        geometry = "",
        traces = records.map(::toTrace),
      )
      
      return result
    }.onFailure {
      Logger.error(context, TAG, "An error occurred while trying to get last record: $it")
    }.getOrDefault(null)

    return null
  }

  private suspend fun loadRecords(): List<Record> {
    Logger.debug(context, TAG, "*** Retrieving records ***")

    return runCatching { RecordCache.getInstance(context).entries() }
      .onFailure { Logger.error(context, TAG, "An error occurred while retrieving records: $it") }
      .getOrDefault(emptyList())
  }

  private fun getCoordinate(location: SimpleLocation): List<Double> {
    return listOf(location.longitude, location.latitude)
  }

  private fun buildMetrics(records: List<Record>): Metrics {
    if (records.size < minimumPoints) {
      return Metrics.default()
    }

    val distances = mutableListOf<Float>()
    for (index in records.indices) {
      if (index != records.lastIndex) {
        val startLocation = records[index].location
        val nextLocation = records[index + 1].location
        startLocation.distanceTo(nextLocation).also { distances.add(it) }
      }
    }

    val times = records.map { (it.location.time / 1000).toDouble() }

    val timeDeltas = mutableListOf<Double>()
    var index = 0
    while (index < times.lastIndex) {
      timeDeltas.add(times[index + 1] - times[index])
      index++
    }

    if (distances.size != timeDeltas.size) {
      throw RuntimeException("distances size: ${distances.size} != timeDeltas size: ${timeDeltas.size}")
    }

    val speeds = mutableListOf<Double>()
    for (i in distances.indices) {
      speeds.add(distances[i] / timeDeltas[i])
    }

    // Get the origin and destination locations
    val origin = records.first()
    val destination = records.last()
    val duration = destination.writeTime - origin.writeTime
    val averageSpeed = speeds.average()

    /*
     * Note:
     * speed is in meters per seconds (m/s)
     * duration is in seconds
     * distance is in meters
     * startedAt and completedAt are local datetime string
     */
    return Metrics(
      speed = speeds.average(),
      duration = destination.writeTime - origin.writeTime,
      distance = (distances.sum() * 0.001),
      calories = MetricsManager.getCalories(averageSpeed, duration),
      startedAt = destination.fmt,
      completedAt = origin.fmt
    )
  }

  private fun toTrace(record: Record): Trace = Trace(
    timezone = record.timezone,
    writeTime = record.writeTime,
    location = SensorLocation(
      latitude = record.location.latitude,
      longitude = record.location.longitude,
      altitude = record.location.altitude,
      time = record.location.time,
      speed = record.location.speed.toDouble(),
      accuracy = record.location.accuracy.toDouble(),
      bearing = record.location.bearing.toDouble()
    )
  )

  companion object {
    private const val TAG = "Analyser"
  }
}

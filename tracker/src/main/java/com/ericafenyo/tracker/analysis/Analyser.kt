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
import com.ericafenyo.tracker.database.entities.AdventureEntity
import com.ericafenyo.tracker.database.CacheDatabase
import com.ericafenyo.tracker.R
import com.ericafenyo.tracker.data.Adventure
import com.ericafenyo.tracker.data.Metrics
import com.ericafenyo.tracker.data.model.FeatureCollection
import com.ericafenyo.tracker.data.model.LineString
import com.ericafenyo.tracker.data.model.Point
import com.ericafenyo.tracker.data.model.Result
import com.ericafenyo.tracker.datastore.Record
import com.ericafenyo.tracker.datastore.RecordCache
import com.ericafenyo.tracker.location.SimpleLocation
import com.ericafenyo.tracker.logger.Logger
import com.ericafenyo.tracker.util.Identity
import com.ericafenyo.tracker.util.getExplicitIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * This class contain methods for reading raw gps data and converting them into a GeoJson data.
 * Our gps data is stored in a local SQLite database as a list of [SimpleLocation] with some metadata.
 *
 * @see generateAdventure
 * @see getCoordinate
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-01-25
 */
class Analyser constructor(private val context: Context) {
  private val computation = Dispatchers.Default
  private val io = Dispatchers.IO

  fun startAnalysis() = runBlocking {
    generateAdventure().run { saveAdventure(this) }
  }

  private suspend fun loadRecords(): List<Record> {
    Logger.debug(context, TAG, "*** Retrieving records ***")
    return try {
      RecordCache.getInstance(context).getAll()
    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to retrieve records: $exception")
      emptyList()
    }
  }

  private suspend fun generateAdventure(): Adventure? = withContext(computation) {
    val records = loadRecords()

    // We need at least ten records to build our feature collections.
    // TODO: 06/05/2021 How many points determine a valid point?
    try {
      if (records.size < 10) {
        Logger.debug(context, TAG, "Invalid record list, returning empty list")
        throw RuntimeException("Invalid record list, returning empty list")
      }

      // Create LineString feature for trip path
      val coordinates = records.map { record -> record.location.getCoordinate() }

      // Create start feature point
      val startFeature = coordinates.first().run { Point(coordinates = this).toFeature() }
      val endFeature = coordinates.last().run { Point(coordinates = this).toFeature() }
      val lineStringFeature = LineString(coordinates = coordinates).toFeature()
      val metrics = buildMetrics(records)
      val featureCollection = FeatureCollection(listOf(startFeature, endFeature, lineStringFeature))

      val adventure = Adventure(
        id = "Unprocessed-${Identity.generateObjectId()}",
        title = "Untitled",
        speed = metrics.speed,
        duration = metrics.duration,
        distance = metrics.distance,
        calories = metrics.calories,
        startedAt = metrics.startedAt,
        imageUrl = "",
        geojson = featureCollection.toJson(),
        completedAt = metrics.completedAt,
      )
      return@withContext adventure
    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to get last record: $exception")
      return@withContext null
    }
  }

  private fun getCoordinate(location: SimpleLocation): List<Double> {
    return listOf(location.longitude, location.latitude)
  }

  private suspend fun saveAdventure(adventure: Adventure?): Result<Boolean> = withContext(io) {
    try {
      if (adventure != null) {
        Timber.d("Adventure $adventure")
        CacheDatabase.getInstance(context)
          .getAdventureDao().insert(AdventureEntity.fromAdventure(adventure))
        Result.Success(true)
      } else {
        Result.Success(false)
      }
    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to save documents: $exception")
      Result.Error(exception)
    } finally {
      context.sendBroadcast(context.getExplicitIntent(R.string.tracker_action_end_analysis))
    }
  }

  fun buildMetrics(records: List<Record>): Metrics {
    if (records.size < 2) {
      return Metrics.default()
    }

    val distances = mutableListOf<Float>()
    for (index in records.indices) {
      if (index != records.size - 1) {
        val startLocation = records[index].location
        val nextLocation = records[index + 1].location
        startLocation.distanceTo(nextLocation).also { distances.add(it) }
      }
    }

    val times = records.map { it.ts }

    val timeDeltas = mutableListOf<Double>()
    var index = 0
    while (index < (times.size - 1)) {
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
    val duration = destination.ts - origin.ts
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
      duration = destination.ts - origin.ts,
      distance = (distances.sum() * 0.001),
      calories = MetricsManager.getCalories(averageSpeed, duration),
      startedAt = destination.fmt,
      completedAt = origin.fmt
    )
  }

  companion object {
    private const val TAG = "Analyser"
  }
}

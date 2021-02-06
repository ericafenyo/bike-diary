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
import com.ericafenyo.tracker.database.Record
import com.ericafenyo.tracker.database.RecordCache
import com.ericafenyo.tracker.location.SimpleLocation
import com.ericafenyo.tracker.logger.Logger
import com.ericafenyo.tracker.util.JSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This class contain methods for reading raw gps data and converting them into a GeoJson data.
 * Our gps data is stored in a local SQLite database as a list of [SimpleLocation] with some metadata.
 *
 * @see generateDocuments
 * @see getCoordinate
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-01-25
 */
class Analyser private constructor(private val context: Context) {
  private val computation = Dispatchers.Default
  private val io = Dispatchers.IO

  // These keys points to a location in the database.
  // 1. We first get all fields containing any of the keys. (@see getRecords())
  // 2. Then after the analysis, we used them to clear the database.
  private val keys = listOf(
    RecordCache.KEY_LOCATION,
    RecordCache.KEY_TRIP_STARTED,
    RecordCache.KEY_TRIP_STOPPED,
  )

  suspend fun analyse() {
    generateDocuments().also { saveDocuments(it) }
  }

  private fun getRecords(): List<Record> {
    Logger.debug(context, TAG, "***Retrieving records***")
    // Suppress Return should be lifted out of 'try'
    return try {
      RecordCache.getInstance(context).getSensorData(keys)
    } catch (exception: Exception) {
      Logger.error(context, TAG, "Error occurred while retrieving records: $exception")
      emptyList()
    }
  }

  private fun segmentRecords(records: List<Record>): List<List<Record>> {
    Logger.debug(context, TAG, "***Segmenting records into trips***")

    try {
      val trips = mutableListOf<List<Record>>()
      var canBeginTrip = true
      var startIndex = 0
      var endIndex = 0
      var processingIndex = 0

      while (processingIndex < records.size) {
        if (canBeginTrip) {
          // Look for the trip start and store its index in the startIndex variable
          records.slice(processingIndex until records.size)
            .indexOfFirst { record -> record.key == RecordCache.KEY_TRIP_STARTED }
            .also { foundStartIndex ->
              if (foundStartIndex == -1) {
                // Properly log the right message
                if (trips.isEmpty()) {
                  Logger.debug(context, TAG, "Trip start not found, exiting")
                } else {
                  Logger.debug(context, TAG, "No further trip start found, exiting")
                }
                processingIndex = records.size
              } else {
                // Set appropriate properties
                startIndex = foundStartIndex + 1 // +1 because next index contains the actual data
                Logger.debug(context, TAG, "Found trip starting at index: $startIndex")
                processingIndex += (startIndex)
                canBeginTrip = false
              }
            }
        } else {
          // Already processed trip start, move on to trip end
          records.slice(processingIndex until records.size)
            .indexOfFirst { record -> record.key == RecordCache.KEY_TRIP_STOPPED }
            .also { foundEndIndex ->
              if (foundEndIndex == -1) {
                Logger.debug(context, TAG, "Can't find end for trip starting at index: $startIndex")
                processingIndex = records.size
              } else {
                // Set appropriate properties
                endIndex = foundEndIndex - 1 // -1 because the previous index have the actual data
                Logger.debug(context, TAG, "Found trip ending at index: $endIndex")
                processingIndex += (endIndex)

                val trip = records.slice(startIndex..endIndex)
                trips.add(trip)
                canBeginTrip = true
              }
            }
        }
      }
      return trips
    } catch (exception: Exception) {
      Logger.error(context, TAG, "Error occurred while segmenting records: $exception")
      return emptyList()
    }
  }

  private suspend fun generateDocuments(): List<FeatureCollection> = withContext(computation) {
    val segments = segmentRecords(getRecords())

    val features: MutableList<FeatureCollection> = mutableListOf()
    for (segment in segments) {
      // Create start feature point
      val startCoordinates = getCoordinate(segment.first().data)
      val startFeature = Point(startCoordinates).toFeature()

      // Create destination feature point
      val endCoordinates = getCoordinate(segment.last().data)
      val endFeature = Point(endCoordinates).toFeature()

      // Create LineString feature for trip path
      val coordinates = segment.map { getCoordinate(it.data) }
      val properties = generateProperties(segment)
      val lineStringFeature = LineString(coordinates = coordinates).toFeature(properties)
      val collections = FeatureCollection(
        features = listOf(startFeature, endFeature, lineStringFeature)
      )
      features.add(collections)
    }

    return@withContext features
  }

  private fun getCoordinate(data: String): List<Double> {
    val location = JSON.parse(data, SimpleLocation::class)
    return listOf(location.longitude, location.latitude)
  }

  private suspend fun saveDocuments(collections: List<FeatureCollection>) = withContext(io) {
    RecordCache.getInstance(context).run {
      putDocuments(RecordCache.KEY_COLLECTION, collections)
      clear(keys)
    }
  }

  private fun generateProperties(records: List<Record>): LinkedHashMap<String, Any> {
    val properties = linkedMapOf<String, Any>()

    val distances = mutableListOf<Float>()
    for (index in records.indices) {
      if (index != records.size - 1) {
        val location = JSON.parse(records[index].data, SimpleLocation::class)
        val nextLocation = JSON.parse(records[index + 1].data, SimpleLocation::class)
        location.distanceTo(nextLocation).also { distances.add(it) }
      }
    }

    val times = records.map { it.ts }

    val timeDeltas = mutableListOf<Double>()
    var index = 0
    while (index < (times.size - 1)) {
      timeDeltas.add(times[index + 1] - times[index]);
      index++
    }

    if (distances.size != timeDeltas.size) {
      throw RuntimeException("distances size: ${distances.size} != timeDeltas size: ${timeDeltas.size}")
    }

    val speeds = mutableListOf<Double>()
    for (i in distances.indices) {
      speeds.add(distances[i] / timeDeltas[i])
    }

    // Duration
    val startTs = locationFromRecord(records.first()).ts
    val endTs = locationFromRecord(records.last()).ts
    val duration = endTs - startTs

    properties["speed"] = (speeds.average() * 3.6)
    properties["duration"] = duration
    properties["distance"] = (distances.sum() * 0.001)
    properties["calories"] = 0

    return properties
  }

  private fun locationFromRecord(record: Record): SimpleLocation {
    return JSON.parse(record.data, SimpleLocation::class)
  }

  companion object {
    private const val TAG = "Analyser"

    @Volatile
    private var INSTANCE: Analyser? = null

    @JvmStatic
    fun getInstance(context: Context): Analyser {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Analyser(context)
          .also { INSTANCE = it }
      }
    }
  }
}

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
import com.ericafenyo.ktx.toFixed
import com.ericafenyo.tracker.BuildConfig
import com.ericafenyo.tracker.R
import com.ericafenyo.tracker.datastore.Record
import com.ericafenyo.tracker.datastore.RecordCache
import com.ericafenyo.tracker.location.SimpleLocation
import com.ericafenyo.tracker.logger.Logger
import com.ericafenyo.tracker.model.FeatureCollection
import com.ericafenyo.tracker.model.LatLng
import com.ericafenyo.tracker.model.LineString
import com.ericafenyo.tracker.model.Point
import com.ericafenyo.tracker.util.JSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import model.ExplicitIntent
import timber.log.Timber

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

  fun startAnalysis() = runBlocking {
    generateDocuments().also { saveDocuments(it) }
  }

  private fun getAllRecords(): List<Record> {
    Logger.debug(context, TAG, "***Retrieving records***")
    // Suppress Return should be lifted out of 'try'
    return try {
      RecordCache.getInstance(context).getSensorData(keys)
    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to retrieve records: $exception")
      emptyList()
    }
  }

  private fun getLastRecord(): List<Record> {
    Logger.debug(context, TAG, "***Getting last record***")

    val records = getAllRecords()
    try {
      var startIndex = 0
      var endIndex = 0
      var canBeginTrip = true
      var processing = true
      val trips = mutableListOf<Record>()

      while (processing) {
        if (canBeginTrip) {
          // Look for the trip start and store its index in the startIndex variable
          records.indexOfLast { record -> record.key == RecordCache.KEY_TRIP_STARTED }
            .also { foundStartIndex ->
              if (foundStartIndex == -1) {
                // Properly log the right message
                Logger.debug(context, TAG, "Trip start not found, exiting")
                return emptyList()
              } else {
                // Set appropriate properties
                startIndex = foundStartIndex + 1 // +1 because next index contains the actual data
                Logger.debug(context, TAG, "Found trip starting at index: $startIndex")
                canBeginTrip = false
                processing = true
              }
            }
        } else {
          // Already processed trip start, move on to trip end
          records.indexOfLast { record -> record.key == RecordCache.KEY_TRIP_STOPPED }
            .also { foundEndIndex ->
              if (foundEndIndex == -1) {
                Logger.debug(context, TAG, "Can't find end for trip starting at index: $startIndex")
                // break the while loop
                processing = false
              } else {
                // Set appropriate properties
                endIndex = foundEndIndex - 1 // -1 because the previous index have the actual data
                Logger.debug(context, TAG, "Found trip ending at index: $endIndex")
                canBeginTrip = true
                processing = false
                val results = records.slice(startIndex..endIndex)
                trips.clear()
                trips.addAll(results)
              }
            }
        }
      }
      return trips
    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to get last record: $exception")
      return emptyList()
    }
  }

  private suspend fun generateDocuments(): List<Any> = withContext(computation) {
    val segment = getLastRecord()

    // We need at least two records to build our starting and ending Point features
    try {
      if (segment.size < 2) {
        Logger.debug(context, TAG, "Invalid record list, returning empty list")
        return@withContext emptyList()
      }

      val featureCollection: MutableList<Any> = mutableListOf()
      // Create LineString feature for trip path
      val coordinates = segment.map { getCoordinate(it.data) }
      val fivePointAccuracyCoordinates = coordinates.map { lnglat ->
        val (lng, lat) = lnglat
        LatLng(lat.toFixed(5), lng.toFixed(5))
      }

      val mapboxBaseUrl = "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static"
      val geojsonSource = "/path-5+f44(${PolylineEncoding.encode(fivePointAccuracyCoordinates)})"
      val path = "/auto/360x240@2x?access_token=${BuildConfig.MAPBOX_ACCESS_TOKEN}"
      val staticMapUrl = "$mapboxBaseUrl$geojsonSource$path"

      Timber.d("FivePointAccuracyCoordinates $fivePointAccuracyCoordinates")
      Timber.d("FivePointAccuracyCoordinates $staticMapUrl")

      // Create start feature point
      val startFeature = coordinates.first().run { Point(coordinates = this).toFeature() }
      val endFeature = coordinates.last().run { Point(coordinates = this).toFeature() }
      val lineStringFeature = LineString(coordinates = coordinates).toFeature()
      val properties = generateProperties(segment)
      val features = FeatureCollection(listOf(startFeature, endFeature, lineStringFeature))

      val dataPack = LinkedHashMap<String, Any>()
      dataPack["data"] = features
      dataPack["metadata"] = properties
      featureCollection.add(dataPack)
      return@withContext featureCollection

    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to get last record: $exception")
      emptyList()
    }
  }

  private fun getCoordinate(data: String): List<Double> {
    val location = JSON.parse(data, SimpleLocation::class)
    return listOf(location.longitude, location.latitude)
  }

  private suspend fun saveDocuments(collections: List<Any>) = withContext(io) {
    try {
      if (collections.isEmpty()) {
        // Result(Exception(""))
      }
      RecordCache.getInstance(context).apply {
        putDocuments(RecordCache.KEY_COLLECTION, collections)
        clear(keys)
      }
      context.sendBroadcast(ExplicitIntent(context, R.string.tracker_action_end_analysis))
    } catch (exception: Exception) {
      Logger.error(context, TAG, "An error occurred while trying to save documents: $exception")
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

    // Duration
    val startTs = locationFromRecord(records.first()).ts
    val endTs = locationFromRecord(records.last()).ts
    val duration = endTs - startTs
    val averageSpeed = speeds.average()

    properties["speed"] = averageSpeed.toFixed(2)
    properties["duration"] = duration
    properties["distance"] = (distances.sum() * 0.001)
    properties["calories"] = Metrics.getCalories(averageSpeed, duration)
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

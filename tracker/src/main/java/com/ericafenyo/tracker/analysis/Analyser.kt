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
import android.util.Log
import com.ericafenyo.tracker.database.Record
import com.ericafenyo.tracker.database.RecordCache
import com.ericafenyo.tracker.location.SimpleLocation
import com.ericafenyo.tracker.logger.Logger
import com.ericafenyo.tracker.util.JSON


class Analyser private constructor(private val context: Context) {

  fun analyse() {
    generateDocuments()
  }

  private fun getRecords(): List<Record> {
    Logger.debug(context, TAG, "***Retrieving records***")
    // Suppress Return should be lifted out of 'try'
    try {
      val keys = listOf(
        RecordCache.KEY_LOCATION,
        RecordCache.KEY_TRIP_STARTED,
        RecordCache.KEY_TRIP_STOPPED,
      )
      return RecordCache.getInstance(context)
        .getSensorData(keys)
        .filterIsInstance<Record>()
    } catch (exception: Exception) {
      Logger.error(context, TAG, "Error occurred while retrieving records: $exception")
      return emptyList()
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

  private fun generateDocuments(): List<FeatureCollection> {
    val segments = segmentRecords(getRecords())

    val features: List<Feature> = mutableListOf()
    for (segment in segments) {

      // Create start feature point
      val startCoordinates = getCoordinate(segment.first().data)
      val startFeature = Point(startCoordinates).toFeature()

      // Create destination feature point
      val endCoordinates = getCoordinate(segment.last().data)
      val endFeature = Point(endCoordinates).toFeature()

      // Create LineString feature for trip path
      val coordinates = segment.map { getCoordinate(it.data) }
      val lineStringFeature = LineString(coordinates = coordinates).toFeature()
      val collections = FeatureCollection(
        features = listOf(startFeature, endFeature, lineStringFeature)
      )
      Log.d("Analyser", "generateDocuments ${JSON.prettify(collections)}")
    }

    return emptyList()
  }

  private fun getCoordinate(data: String): List<Double> {
    val location = JSON.parse(data, SimpleLocation::class)
    return listOf(location.longitude, location.latitude)
  }

  private fun saveDocuments(collects: List<FeatureCollection>) {

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

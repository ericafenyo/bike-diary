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

package com.ericafenyo.habitdiary.data.trip

import com.ericafenyo.habitdiary.model.Trip
import com.ericafenyo.tracker.analysis.FeatureCollection
import com.ericafenyo.tracker.database.Record
import com.ericafenyo.tracker.database.TrackerDataSource
import com.ericafenyo.tracker.util.JSON
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

interface TripRepository {
  suspend fun currentTrip(tripId: String): Trip
  fun observeTrip(tripId: String): Flow<Trip>
  fun observeTrips(): Flow<List<Trip>>
}

class TripRepositoryImpl @Inject constructor(
  private val trackerDataSource: TrackerDataSource
) : TripRepository {
  override suspend fun currentTrip(tripId: String): Trip {
    TODO("Not yet implemented")
  }

  override fun observeTrip(tripId: String): Flow<Trip> {
    TODO("Not yet implemented")
  }

  override fun observeTrips(): Flow<List<Trip>> {
    return trackerDataSource.getRecords()
      .mapLatest(::toTrips)
  }

  private fun toTrips(records: List<Record>): List<Trip> {
    val trips = mutableListOf<Trip>()
    records.forEach {
      val data = it.data
      try {
        val collections = JSON.parse(data, FeatureCollection::class)
        val lineFeature = collections.features.find { t -> t.geometry.type == "LineString" }
        if (lineFeature != null) {
          val properties = lineFeature.properties
          val speed = properties["duration"] as Double
          val duration = properties["duration"] as Double
          val distance = properties["distance"] as Double
          val calories = properties["calories"] as Double
          Timber.d("Features oo $speed $duration $distance $calories")
          val trip = Trip(
            image = "",
            title = "Nantes to Pornic",
            calories = calories,
            duration = duration,
            distance = distance,
            speed = speed,
          )
          trips.add(trip)
        } else {
          return emptyList()
        }
      } catch (e: Exception) {
        Timber.e(e, "Features oo")
        return emptyList()
      }
    }
    return trips
  }
}

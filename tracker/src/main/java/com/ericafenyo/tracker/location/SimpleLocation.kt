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

package com.ericafenyo.tracker.location

import android.location.Location
import kotlinx.serialization.Serializable

@Serializable
data class SimpleLocation(
  val latitude: Double,
  val longitude: Double,
  val altitude: Double,
  val ts: Double,
  val speed: Float,
  val accuracy: Float,
  val bearing: Float
) {

  /**
   * Computes the distance in meters between this and the given destination location.
   * @param dest the destination location
   */
  fun distanceTo(dest: SimpleLocation): Float {
    val results = FloatArray(1)
    Location.distanceBetween(latitude, longitude, dest.latitude, dest.longitude, results)
    return results[0]
  }

  /**
   * Returns a list containing the Location's [longitude] and [latitude]
   */
  fun getCoordinate(): List<Double> = listOf(longitude, latitude)
}

fun Location.simplify() = SimpleLocation(
  latitude = latitude,
  longitude = longitude,
  altitude = altitude,
  ts = time.toDouble(),
  speed = speed,
  accuracy = accuracy,
  bearing = bearing,
)

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

import com.ericafenyo.bikediary.model.SimpleMetrics
import com.ericafenyo.tracker.location.SimpleLocation
import kotlin.math.roundToInt

/**
 * Contains variety of methods for metric conversions.
 */
internal object MetricsManager {
  /**
   * Returns the number of calories burnt per minute during a cycling activity.
   *
   * @param speed   speed in meters per second
   * @param duration   duration in seconds
   */
  @JvmStatic
  fun getCalories(speed: Double, duration: Double): Int {
    val metValue = getMetValue(metersPerSecondsToMilesPerHour(speed))
    val weight = 68.0 // body weight in kilograms
    // Formula ((MET * W * 3.5) / 200) * T
    // Where W = body weight in Kg and T = duration in minutes
    // 3.5 and 200 are magic numbers.
    val caloriesPerMinute = ((metValue * weight * 3.5) / 200) * secondsToMinutes(duration)
    return caloriesPerMinute.roundToInt()
  }

  /**
   * Converts seconds to minutes.
   *
   * @param duration  duration in seconds
   * @return returns duration in minutes
   */
  @JvmStatic
  fun secondsToMinutes(duration: Double): Double = duration / 60

  /**
   *Converts meters per seconds to miles per hour.
   * Note: There are 2.2369362921 mph in 1 m/s.
   * @param speed a speed in meters per second (m/s)
   * @return a speed in miles per hour (mph)
   */
  @JvmStatic
  fun metersPerSecondsToMilesPerHour(speed: Double): Double {
    return if (speed >= 0) 2.2369362921 * speed else 0.0
  }

  fun metersPerSecondsToKilometersPerHour(speed: Double): Double {
    return if (speed >= 0) 3.6 * speed else 0.0
  }

  fun metersToKilometers(distance: Double): Double {
    return if (distance >= 0) distance / distance else 0.0
  }

  /**
   * Returns the Metabolic Equivalent of Task (MET) for a given cycling speed.
   * MET is a measurement of the energy cost of physical activity for a period of time.
   *
   * @param speed cycling speed in miles per hour
   *
   * @see [Cyclist's MET Values](https://captaincalculator.com/health/calorie/calories-burned-cycling-calculator)
   */
  private fun getMetValue(speed: Double): Double {
    return when {
      speed > 0.0 && speed < 5.5 -> 3.0
      speed >= 5.5 && speed < 9.4 -> 3.5
      speed >= 9.4 && speed < 10 -> 5.8
      speed >= 10 && speed < 12 -> 6.8
      speed >= 12 && speed < 14 -> 8.0
      speed >= 14 && speed < 16 -> 10.0
      speed >= 16 && speed < 19 -> 12.0
      speed >= 19 && speed < 20 -> 15.0
      speed >= 20 -> 16.0
      else -> 0.0
    }
  }


  fun getLiveMetrics(locations: List<SimpleLocation>): SimpleMetrics {
    if (locations.size < 2) {
      return SimpleMetrics(speed = 0.0, distance = 0.0, duration = 0.0, calories = 0)
    }

    val distances = mutableListOf<Float>()
    for (index in locations.indices) {
      if (index != locations.size - 1) {
        val startLocation = locations[index]
        val nextLocation = locations[index + 1]
        startLocation.distanceTo(nextLocation).also { distances.add(it) }
      }
    }

    val times = locations.map { (it.time/1000).toDouble() }

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
    val origin = locations.first()
    val destination = locations.last()
    val duration = destination.time - origin.time
    val averageSpeed = speeds.average()

    /*
     * Note:
     * speed is in meters per seconds (km/h).
     * duration varies between seconds, minutes and hours.
     * distance is in kilometers (km)
     */
    return SimpleMetrics(
      speed = metersPerSecondsToKilometersPerHour(destination.speed.toDouble()),
      duration = (destination.time/1000).toDouble() - (origin.time/1000).toDouble(),
      distance = (distances.sum() * 0.001),
      calories = getCalories(averageSpeed, duration.toDouble()),
    )
  }
}

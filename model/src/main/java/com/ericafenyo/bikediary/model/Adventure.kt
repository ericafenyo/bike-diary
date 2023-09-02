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

package com.ericafenyo.bikediary.model

import java.time.Instant

/**
 * An `Adventure` represents a cycling trip.
 */
data class Adventure(
  /**
   * A unique identifier for the adventure.
   */
  val id: String,
  /**
   * Represents the name or title of the adventure.
   */
  val title: String,
  /**
   * A statement describing the adventure.
   */
  val description: String,
  /**
   * Represents the start date and time of the adventure.
   */
  val startTime: Instant,
  /**
   * Represents the end date and time of the adventure.
   */
  val endTime: Instant,
  /**
   * Maximum altitude reached during the adventure.
   */
  val altitude: Double,
  /**
   * Distance traveled during the adventure
   */
  val distance: Double,
  /**
   * duration of the adventure in seconds
   */
  val duration: Long,
  /**
   * Average speed during the adventure
   */
  val speed: Double,
  /**
   * Estimated number of calories burned during the adventure
   */
  val calories: Int,

  val image: String,
)

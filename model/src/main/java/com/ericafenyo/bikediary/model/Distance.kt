/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2023 Eric Afenyo
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


/**
 * Indicates the distance traveled over an interval.
 */
class Distance private constructor(
  private val value: Double,
  private val type: Type,
) : Comparable<Distance> {

  /** Returns the distance in meters. */
  val meters: Double
    get() = value * 1000.0

  /** Returns the distance in kilometers. */
  val kilometers: Double
    get() = get(type = Type.KILOMETERS)

  /** Returns the distance in miles. */
  val miles: Double
    get() = get(type = Type.MILES)

  private fun get(type: Type): Double {
    if (this.type == type) return value

    return when (type) {
      Type.METERS -> value * 1000.0
      Type.KILOMETERS -> value / 1000.0
      Type.MILES -> value / 1609.34
    }
  }

  private enum class Type {
    METERS {
      override val title: String = "m"
    },
    KILOMETERS {
      override val title: String = "km"
    },
    MILES {
      override val title: String = "mi"
    };

    abstract val title: String
  }

  override fun compareTo(other: Distance): Int =
    if (type == other.type) {
      value.compareTo(other.value)
    } else {
      meters.compareTo(other.meters)
    }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Distance) return false

    if (type == other.type) {
      return value == other.value
    }

    return meters == other.meters
  }

  override fun toString(): String = "$value ${type.title}"

  override fun hashCode(): Int = meters.hashCode()

  companion object {
    /** Creates [Distance] with the specified value in meters. */
    fun meters(value: Double): Distance = Distance(value, Type.METERS)

    /** Creates [Distance] with the specified value in kilometers. */
    fun kilometers(value: Double): Distance = Distance(value, Type.KILOMETERS)

    /** Creates [Distance] with the specified value in miles. */
    fun miles(value: Double): Distance = Distance(value, Type.MILES)
  }
}

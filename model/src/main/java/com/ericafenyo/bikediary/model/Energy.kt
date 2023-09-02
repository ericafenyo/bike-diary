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
 * Estimated active energy burnt.
 */
class Energy private constructor(
  private val value: Double,
  private val type: Type,
) : Comparable<Energy> {

  /** Returns the energy in calories. */
  val calories: Double
    get() = if (this.type == Type.CALORIES) value else value * 1000.0

  /** Returns the energy in kilocalories. */
  val kilocalories: Double
    get() = if (this.type == Type.KILOCALORIES) value else value / 1000.0

  override fun compareTo(other: Energy): Int =
    if (type == other.type) {
      value.compareTo(other.value)
    } else {
      calories.compareTo(other.calories)
    }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Energy) return false

    if (type == other.type) {
      return value == other.value
    }

    return calories == other.calories
  }

  override fun hashCode(): Int = calories.hashCode()

  override fun toString(): String = "$value ${type.title}"

  companion object {
    /** Returns zero [Energy] of the same [Type.CALORIES]. */
    fun zero(): Energy = Energy(0.0, Type.CALORIES)

    /** Creates [Energy] with the specified value in calories. */
    @JvmStatic
    fun calories(value: Double): Energy = Energy(value, Type.CALORIES)

    /** Creates [Energy] with the specified value in kilocalories. */
    @JvmStatic
    fun kilocalories(value: Double): Energy = Energy(value, Type.KILOCALORIES)
  }

  private enum class Type {
    CALORIES {
      override val title: String = "cal"
    },
    KILOCALORIES {
      override val title: String = "kcal"
    };

    abstract val title: String
  }
}

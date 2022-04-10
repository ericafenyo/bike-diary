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

import com.ericafenyo.bikediary.model.Gender.UNSPECIFIED
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
  val id: String,
  val gender: Gender,
  val height: Double,
  val weight: Double,
  val quests: Quests,
) {

  class Builder(settings: Settings) {
    private val id: String = settings.id
    var gender: Gender = settings.gender
    var height: Double = settings.height
    var weight: Double = settings.weight
    var calories: CaloriesQuest = settings.quests.calories
    var distance: DistanceQuest = settings.quests.distance

    fun build(): Settings {
      return Settings(
        id = id,
        gender = gender,
        height = height,
        weight = weight,
        quests = Quests(calories = calories, distance = distance)
      )
    }
  }

  companion object {
    val Default = Settings(
      id = "62459ab8da79e2aaf1c2fd82",
      gender = UNSPECIFIED,
      height = 73.0,
      weight = 64.0,
      quests = Quests(
        distance = DistanceQuest(target = 1000.0),
        calories = CaloriesQuest(target = 500)
      )
    )
  }
}

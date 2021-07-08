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

package com.ericafenyo.tracker.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ericafenyo.bikediary.model.Adventure

@Entity(tableName = "adventures")
data class AdventureEntity(
  @PrimaryKey val id: String,
  val title: String,
  val speed: Double,
  val duration: Double,
  val distance: Double,
  val calories: Int,
  val startedAt: String,
  val completedAt: String,
  val geojson: String,
  val imageUrl: String,
) {

  companion object {
    fun fromAdventure(adventure: Adventure) = AdventureEntity(
      id = adventure.id,
      title = adventure.title,
      speed = adventure.speed,
      duration = adventure.duration,
      distance = adventure.distance,
      calories = adventure.calories,
      startedAt = adventure.startedAt,
      completedAt = adventure.completedAt,
      geojson = adventure.geojson,
      imageUrl = adventure.imageUrl,
    )
  }
}

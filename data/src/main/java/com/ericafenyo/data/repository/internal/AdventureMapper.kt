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

package com.ericafenyo.data.repository.internal

import com.ericafenyo.data.api.internal.response.GetAdventuresResponse
import com.ericafenyo.data.database.AdventureEntity
import com.ericafenyo.data.model.Adventure
import com.ericafenyo.data.repository.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdventureMapper @Inject constructor() : Mapper<Any, Adventure> {
  override suspend fun map(params: Any): Adventure {
    return when (params) {
      is AdventureEntity -> fromLocal(params)
      is GetAdventuresResponse -> fromRemote(params)
      else -> throw IllegalArgumentException("Parameter type ${params::class.java} is not supported")
    }
  }

  private fun fromRemote(response: GetAdventuresResponse) = Adventure(
    id = response.id,
    title = response.title,
    speed = response.speed,
    distance = response.distance,
    duration = response.duration,
    calories = response.calories,
    date = response.date,
    geojson = response.geojson,
    images = response.images
  )

  private fun fromLocal(response: AdventureEntity) = Adventure(
    id = "${response.id}",
    title = response.title,
    speed = response.speed,
    distance = response.distance,
    duration = response.duration,
    calories = response.calories,
    date = response.date,
    geojson = response.geojson,
    images = listOf()
  )
}

/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Eric Afenyo
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

package com.ericafenyo.bikediary.network.analysis

import com.apollographql.apollo3.ApolloClient
import com.ericafenyo.bikediary.graphql.CreateAdventuresMutation
import com.ericafenyo.bikediary.network.invoke
import com.ericafenyo.bikediary.graphql.type.AdventureInput
import com.ericafenyo.bikediary.graphql.type.LocationInput
import timber.log.Timber


interface AnalysisService {
  suspend fun synchronize(requests: List<AnalyzedAdventureRequest>): Boolean
}

fun analysisService(client: ApolloClient) = object : AnalysisService {

  override suspend fun synchronize(requests: List<AnalyzedAdventureRequest>): Boolean {
    return runCatching {
      client.mutation(CreateAdventuresMutation(requests.map(this::toInput))).invoke()
      true
    }.getOrElse { error ->
      Timber.d(error)
      false
    }
  }

  @Suppress("NOTHING_TO_INLINE")
  private inline fun toInput(request: AnalyzedAdventureRequest): AdventureInput {
    return AdventureInput(
      uuid = request.uuid,
      title = "",
      description = "",
      altitude = 0.0,
      calories = request.calories,
      distance = request.distance,
      duration = request.duration,
      startTime = request.startTime,
      endTime = request.endTime,
      speed = request.speed,
      polyline = "",
      image = "",
      locations = request.locations.map { location ->
        LocationInput(
          timezone = location.timezone,
          writeTime = location.writeTime,
          latitude = location.latitude,
          longitude = location.longitude,
          altitude = location.altitude,
          time = location.time,
          speed = location.speed,
          accuracy = location.accuracy,
          bearing = location.bearing,
        )
      },
    )
  }
}

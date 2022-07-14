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

package com.ericafenyo.bikediary.network.analysis.internal

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.ericafenyo.bikediary.AddAdventuresMutation
import com.ericafenyo.bikediary.network.ApolloHttpException
import com.ericafenyo.bikediary.network.analysis.AnalysisService
import com.ericafenyo.bikediary.network.analysis.AnalyzedAdventureRequest
import com.ericafenyo.bikediary.network.invoke
import com.ericafenyo.bikediary.type.AdventureInput
import com.ericafenyo.bikediary.type.LocationInput
import com.ericafenyo.bikediary.type.TraceInput
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
internal class AnalysisServiceImpl @Inject constructor(
  private val client: ApolloClient
) : AnalysisService {

  override suspend fun synchronize(requests: List<AnalyzedAdventureRequest>): Boolean {
    try {
      client.mutation(AddAdventuresMutation(requests.map(this::toInput))).invoke()
      return true
    } catch (exception: ApolloException) {
      if (exception is ApolloHttpException) {
        Timber.e("${exception.error.nonStandardFields}")
      }
      return false
    }
  }

  private fun toInput(request: AnalyzedAdventureRequest): AdventureInput {
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
      traces = request.traces.map { trace ->
        TraceInput(
          timezone = trace.timezone,
          writeTime = trace.writeTime,
          location = LocationInput(
            latitude = trace.location.latitude,
            longitude = trace.location.longitude,
            altitude = trace.location.altitude,
            time = trace.location.time,
            speed = trace.location.speed,
            accuracy = trace.location.accuracy,
            bearing = trace.location.bearing,
          )
        )
      },
    )
  }
}

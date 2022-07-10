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

package com.ericafenyo.bikediary.network.adventure.internal

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.ericafenyo.bikediary.GetAdventuresQuery
import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.bikediary.network.ApolloHttpException
import com.ericafenyo.bikediary.network.adventure.AdventureService
import com.ericafenyo.bikediary.network.invoke
import com.ericafenyo.libs.serialization.ReflectionJsonSerializer
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
internal class AdventureServiceImpl @Inject constructor(
  private val apolloClient: ApolloClient,
) : AdventureService {
  private val jsonSerializer = ReflectionJsonSerializer.getInstance()

  override suspend fun getAdventures(): List<Adventure> {

    try {
      val response = apolloClient.query(GetAdventuresQuery()).invoke()
      return response.adventures.map {
        Adventure(
          id = it.id,
          title = it.title,
          description = "",
          speed = it.speed,
          altitude = it.altitude,
          duration = it.duration,
          distance = it.distance,
          calories = 7,
          startTime = Instant.parse(it.startTime),
          endTime = Instant.parse(it.endTime),
          image = "",
        )
      }
    } catch (exception: ApolloException) {
      if (exception is ApolloHttpException) {
        Timber.e("${exception.error.nonStandardFields}")
      }
//      throw toHttpException(exception)
      throw exception
    }
  }


  override suspend fun synchronizeAdventures(adventures: List<Adventure>): List<Adventure> {
//    val input = adventures.map { adventure ->
//      AdventureInput(
//        uuid = adventure.id,
//        title = adventure.title,
//        speed = adventure.speed,
//        duration = adventure.duration,
//        distance = adventure.distance,
//        calories = adventure.calories,
//        startedAt = adventure.startTime,
//        completedAt = adventure.endTime,
//        geojson = "",
//        images = listOf(),
//      )
//    }

//    val response = apolloClient.mutate(AddAdventuresMutation(input)).await()
//
//    if (response.hasErrors()) {
//      val map = response.errors?.first()?.customAttributes?.toMutableMap() ?: mutableMapOf()
//      val json = JSONObject(map).toString()
//      val apolloError = jsonSerializer.fromJson(json, ApolloErrorResponse::class)
//
//      throw HttpException(
//        status = apolloError.extensions.exception.status,
//        message = apolloError.extensions.exception.message
//      )
//    }
//
//    val data =
//      response.data?.addAdventures ?: throw HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR)

//    return data.map {
//      Adventure(
//        id = it.id,
//        title = it.title,
//        speed = it.speed,
//        duration = it.duration,
//        distance = it.distance,
//        calories = it.calories,
//        startTime = Instant.now(),
//        endTime = Instant.now(),
//        image = "",
//      )
//    }

    return emptyList()
  }
}


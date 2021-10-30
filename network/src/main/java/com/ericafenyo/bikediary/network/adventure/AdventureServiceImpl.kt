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

package com.ericafenyo.bikediary.network.adventure

import AddAdventuresMutation
import GetAdventuresQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.bikediary.model.HttpException
import com.ericafenyo.bikediary.network.ApolloErrorResponse
import com.ericafenyo.libs.serialization.ReflectionJsonSerializer
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton
import org.json.JSONObject
import type.AdventureInput

@Singleton
class AdventureServiceImpl @Inject constructor(
  private val apolloClient: ApolloClient,
) : AdventureService {
  private val jsonSerializer = ReflectionJsonSerializer.getInstance()

  override suspend fun getAdventures(): List<Adventure> {
    val response = apolloClient.query(GetAdventuresQuery()).await()

    if (response.hasErrors()) {
      val map = response.errors?.first()?.customAttributes?.toMutableMap() ?: mutableMapOf()
      val json = JSONObject(map).toString()
      val apolloError = jsonSerializer.fromJson(json, ApolloErrorResponse::class)

      throw HttpException(
        status = apolloError.extensions.exception.status,
        message = apolloError.extensions.exception.message
      )
    }

    val data =
      response.data?.adventures ?: throw HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR)

    return data.map {
      Adventure(
        id = it.id,
        uuid = it.uuid,
        title = it.title,
        speed = it.speed,
        duration = it.duration,
        distance = it.distance,
        calories = it.calories,
        startedAt = it.startedAt as String,
        completedAt = it.completedAt as String,
        geojson = it.geojson,
        images = listOf(),
      )
    }
  }

  override suspend fun synchronizeAdventures(adventures: List<Adventure>): List<Adventure> {
    val input = adventures.map { adventure ->
      AdventureInput(
        uuid = adventure.uuid,
        title = adventure.title,
        speed = adventure.speed,
        duration = adventure.duration,
        distance = adventure.distance,
        calories = adventure.calories,
        startedAt = adventure.startedAt,
        completedAt = adventure.completedAt,
        geojson = adventure.geojson,
        images = adventure.images,
      )
    }

    val response = apolloClient.mutate(AddAdventuresMutation(input)).await()

    if (response.hasErrors()) {
      val map = response.errors?.first()?.customAttributes?.toMutableMap() ?: mutableMapOf()
      val json = JSONObject(map).toString()
      val apolloError = jsonSerializer.fromJson(json, ApolloErrorResponse::class)

      throw HttpException(
        status = apolloError.extensions.exception.status,
        message = apolloError.extensions.exception.message
      )
    }

    val data =
      response.data?.addAdventures ?: throw HttpException(HttpURLConnection.HTTP_INTERNAL_ERROR)

    return data.map {
      Adventure(
        id = it.id,
        uuid = it.uuid,
        title = it.title,
        speed = it.speed,
        duration = it.duration,
        distance = it.distance,
        calories = it.calories,
        startedAt = it.startedAt as String,
        completedAt = it.completedAt as String,
        geojson = it.geojson,
        images = listOf(),
      )
    }
  }
}

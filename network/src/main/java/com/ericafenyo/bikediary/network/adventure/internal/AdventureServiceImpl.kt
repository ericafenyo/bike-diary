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
import com.ericafenyo.bikediary.graphql.CreateAdventuresMutation
import com.ericafenyo.bikediary.graphql.GetAdventureByIdQuery
import com.ericafenyo.bikediary.graphql.GetAdventuresQuery
import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.bikediary.network.adventure.AdventureService
import com.ericafenyo.bikediary.network.invoke
import com.ericafenyo.bikediary.network.toHttpException
import com.ericafenyo.bikediary.graphql.type.AdventureInput
import java.time.Instant

fun adventureService(apolloClient: ApolloClient) = object : AdventureService {

  override suspend fun getAdventures(): List<Adventure> {
    try {
      return apolloClient.query(GetAdventuresQuery()).invoke().adventures.map(::toAdventureModel)
    } catch (exception: ApolloException) {
      throw toHttpException(exception)
    }
  }

  override suspend fun getAdventure(id: String): Adventure {
    try {
      val response = apolloClient.query(GetAdventureByIdQuery(id)).invoke()
      return Adventure(
        id = response.adventure.id,
        title = response.adventure.title,
        description = response.adventure.description,
        speed = response.adventure.speed,
        altitude = 0.0,
        duration = response.adventure.duration,
        distance = response.adventure.distance,
        calories = response.adventure.calories,
        startTime = Instant.parse(response.adventure.startTime),
        endTime = Instant.parse(response.adventure.endTime),
        image = "response.adventure.image",
      )
    } catch (exception: ApolloException) {
      throw toHttpException(exception)
    }
  }

  override suspend fun synchronizeAdventures(adventures: List<Adventure>): List<Adventure> {
    try {
      val inputs = adventures.map { adventure -> createAdventureInput(adventure) }
      val response = apolloClient.mutation(CreateAdventuresMutation(inputs)).invoke()
        .createAdventures.map {
          Adventure(
            id = it.id,
            title = it.title,
            speed = it.speed,
            duration = it.duration,
            distance = it.distance,
            calories = it.calories,
            startTime = Instant.now(),
            endTime = Instant.now(),
            image = "",
            altitude = 0.0,
            description = it.description
          )
        }

      return response
    } catch (exception: ApolloException) {
      throw toHttpException(exception)
    }
  }

}

private fun createAdventureInput(adventure: Adventure) = AdventureInput(
  uuid = adventure.id,
  title = adventure.title,
  speed = adventure.speed,
  duration = adventure.duration,
  distance = adventure.distance,
  calories = adventure.calories,
  startTime = "${adventure.startTime}",
  endTime = "${adventure.endTime}",
  polyline = "",
  image = adventure.image,
  locations = emptyList(),
  altitude = adventure.altitude,
  description = adventure.description
)

@Suppress("NOTHING_TO_INLINE")
private inline fun toAdventureModel(response: GetAdventuresQuery.Adventure) = Adventure(
  id = response.id,
  title = response.title,
  description = "",
  speed = response.speed,
  altitude = 0.0,
  duration = response.duration,
  distance = response.distance,
  calories = response.calories,
  startTime = Instant.parse(response.startTime),
  endTime = Instant.parse(response.endTime),
  image = "response.image",
)


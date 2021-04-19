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

package com.ericafenyo.data.api.internal


import GetAdventuresQuery
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloHttpException
import com.ericafenyo.data.api.BikeDiaryService
import com.ericafenyo.data.api.internal.response.SaveAdventureResponse
import com.ericafenyo.data.model.Adventure


class BikeDiaryServiceImpl(
  private val client: ApolloClient
) : BikeDiaryService {

  override suspend fun getAdventures(): List<Adventure> {
    return try {
      client.query(GetAdventuresQuery())
        .await()
        .data?.adventures()
        ?.map(::toAdventure) ?: emptyList()
    } catch (exception: ApolloHttpException) {
      Log.e("TAG", "An error occurred while fetching adventures ${exception.message()}")
      emptyList()
    }
  }

  override suspend fun saveAdventures(): SaveAdventureResponse {
    TODO("Not yet implemented")
  }

  private fun toAdventure(result: GetAdventuresQuery.Adventure): Adventure {
    return Adventure(
      id = result.id(),
      title = result.title(),
      speed = result.speed(),
      duration = result.duration(),
      calories = result.calories(),
      date = result.date(),
      geojson = result.geojson(),
      images = result.images(),
      distance = 0.0
    )
  }
}

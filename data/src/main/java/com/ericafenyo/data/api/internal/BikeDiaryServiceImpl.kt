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


import CreateUserMutation
import CreateUserMutation.Data
import GetAdventuresQuery
import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloHttpException
import com.ericafenyo.data.BuildConfig
import com.ericafenyo.data.api.internal.response.SaveAdventureResponse
import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.tracker.data.api.BikeDiaryService
import com.ericafenyo.tracker.data.api.vo.CreateUserRequest
import com.ericafenyo.tracker.data.api.vo.CreateUserResponse
import java.io.File
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import okio.buffer
import okio.sink
import timber.log.Timber
import type.UserInput

class BikeDiaryServiceImpl(
  private val apolloClient: ApolloClient,
  private val httpClient: OkHttpClient,
  private val context: Context
) : BikeDiaryService {

  override suspend fun getAdventures(): List<Adventure> {
    return try {
      apolloClient.query(GetAdventuresQuery())
        .await()
        .data?.adventures
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
      id = result.id,
      title = result.title,
      speed = result.speed,
      duration = result.duration,
      distance = result.distance,
      calories = result.calories.toInt(),
      startedAt = result.startedAt,
      completedAt = result.completedAt,
      geojson = result.geojson,
      imageUrl = result.imageUrl,
    )
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun getStaticMap(geojson: String): String {
    val mapboxBaseUrl = BuildConfig.MAPBOX_STATIC_MAP_URL
    val geojsonSource = "/geojson($geojson)"
    val path = "/auto/360x240@2x?access_token=${BuildConfig.MAPBOX_ACCESS_TOKEN}"
    val staticMapUrl = "$mapboxBaseUrl$geojsonSource$path"

    val request = Request.Builder()
      .url(staticMapUrl)
      .get()
      .build()

    try {
      val response = httpClient.newCall(request).execute()
      val responseBody = response.body()
      when {
        response.isSuccessful && responseBody != null -> {
          val fileName = "${UUID.randomUUID()}.png".replace("-", "")
          val downloadedFile = File(context.cacheDir, fileName)
          val sink: BufferedSink = downloadedFile.sink().buffer()
          sink.writeAll(responseBody.source())
          sink.close()
          return fileName
        }
        else -> {
          return ""
        }
      }
    } catch (e: Exception) {
      Timber.e(e, "response")
      return ""
    }
  }

  override suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
    return suspendCoroutine { continuation ->
      val userInput = UserInput(
        name = request.name,
        email = request.email,
        password = request.password
      )
      val userMutation = CreateUserMutation(userInput)
      apolloClient.mutate(userMutation).enqueue(object :
        ApolloCall.Callback<Data>() {
        override fun onResponse(response: Response<Data>) {
          if (!response.hasErrors()) {
            val user = response.data?.user
            val useResponse = CreateUserResponse(
              id = user?.id ?: "",
              name = user?.name ?: "",
              email = user?.email ?: "",
              weight = user?.weight ?: 0.4,
              bio = user?.bio ?: "",
              avatarUrl = user?.avatar ?: "",
              gender = "",
              height = 0.0,
            )
            continuation.resume(useResponse)
          } else {
            Timber.e("We have unknown errors")
          }
        }

        override fun onFailure(exception: ApolloException) {
          Timber.e("We have errors  $exception")
          continuation.resumeWithException(exception)
        }
      })
    }
  }
}

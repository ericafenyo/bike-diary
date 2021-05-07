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
import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloHttpException
import com.ericafenyo.data.BuildConfig
import com.ericafenyo.data.api.BikeDiaryService
import com.ericafenyo.data.api.internal.response.SaveAdventureResponse
import com.ericafenyo.tracker.data.Adventure
import java.io.File
import java.util.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSink
import okio.buffer
import okio.sink
import timber.log.Timber

class BikeDiaryServiceImpl(
  private val apolloClient: ApolloClient,
  private val httpClient: OkHttpClient,
  private val context: Context
) : BikeDiaryService {

  override suspend fun getAdventures(): List<Adventure> {
    return try {
      apolloClient.query(GetAdventuresQuery())
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
}

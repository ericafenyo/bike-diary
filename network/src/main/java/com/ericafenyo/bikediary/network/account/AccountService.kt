/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2023 Eric Afenyo
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

package com.ericafenyo.bikediary.network.account

import com.apollographql.apollo3.ApolloClient
import com.ericafenyo.bikediary.graphql.AuthenticateUserMutation
import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.model.HttpException
import com.ericafenyo.bikediary.network.ApolloErrorResponse
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber

interface AccountService {
  suspend fun authenticate(email: String, password: String): Credentials
}

fun accountService(apolloClient: ApolloClient) = object : AccountService {
  override suspend fun authenticate(email: String, password: String): Credentials {
    val response = apolloClient.mutation(AuthenticateUserMutation(email, password)).execute()

    if (response.hasErrors()) {
      val map = response.errors?.first()?.extensions?.toMutableMap() ?: mutableMapOf()
      val json = JSONObject(map).toString()
      val apolloError =
        Json { ignoreUnknownKeys = true }.decodeFromString(ApolloErrorResponse.serializer(), json)


      throw HttpException(
        status = apolloError.exception.status,
        message = apolloError.exception.message
      )
    }

    val data = response.data?.login ?: throw HttpException(400)
    return Credentials(
      accessToken = data.access_token,
    )
  }
}

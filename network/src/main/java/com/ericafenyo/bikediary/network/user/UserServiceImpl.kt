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

package com.ericafenyo.bikediary.network.user

import CreateUserMutation
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.ericafenyo.bikediary.model.HttpException
import com.ericafenyo.bikediary.model.User
import com.ericafenyo.bikediary.network.ApolloErrorResponse
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber
import type.AddUserInput

class UserServiceImpl @Inject constructor(
  private val apolloClient: ApolloClient,
) : UserService {
  private val jsonSerializer = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
  }

  override suspend fun addUser(
    firstName: String,
    lastName: String,
    email: String,
    password: String
  ): User {
    val userInput = AddUserInput(firstName, lastName, email, password)
    try {
      val response = apolloClient.mutate(CreateUserMutation(userInput)).await()
      if (response.hasErrors()) {
        val map = response.errors?.first()?.customAttributes?.toMutableMap() ?: mutableMapOf()
        val jsonObject = JSONObject(map).toString()
        val apolloError: ApolloErrorResponse = jsonSerializer.decodeFromString(jsonObject)

        throw HttpException(
          status = apolloError.extensions.exception.status,
          message = apolloError.extensions.exception.message
        )
      }

      val userId = response.data?.user?.id ?: throw HttpException(HTTP_NOT_FOUND)

      return User(
        id = userId,
        email = email,
        firstName = firstName,
        lastName = lastName,
        bio = "",
        avatarUrl = "",
        weight = 0.0,
      )

    } catch (exception: ApolloException) {
      Timber.d("AddUser: name: ${exception.stackTraceToString()}")
      Timber.d("AddUser: name: ${exception.message}")
      throw HttpException()
    }
  }
}

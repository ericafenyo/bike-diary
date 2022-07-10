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

import com.apollographql.apollo3.ApolloClient
import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.model.User
import com.ericafenyo.libs.serialization.ReflectionJsonSerializer
import javax.inject.Inject

class UserServiceImpl @Inject constructor(
  private val apolloClient: ApolloClient,
) : UserService {
  private val jsonSerializer = ReflectionJsonSerializer.getInstance()

  override suspend fun addUser(
    firstName: String,
    lastName: String,
    email: String,
    password: String
  ): User {
//    val userInput = AddUserInput(firstName, lastName, email, password)
//    try {
//      val response = apolloClient.mutate(CreateUserMutation(userInput)).await()
//      if (response.hasErrors()) {
//        val map = response.errors?.first()?.customAttributes?.toMutableMap() ?: mutableMapOf()
//        val json = JSONObject(map).toString()
//        val apolloError: ApolloErrorResponse =
//          jsonSerializer.fromJson(json, ApolloErrorResponse::class)
//
//        throw HttpException(
//          status = apolloError.extensions.exception.status,
//          message = apolloError.extensions.exception.message
//        )
//      }
//
//      val userId = response.data?.user?.id ?: throw HttpException(HTTP_NOT_FOUND)
//
//      return User(
//        id = userId,
//        email = email,
//        firstName = firstName,
//        lastName = lastName,
//        bio = "",
//        avatarUrl = "",
//        weight = 0.0,
//      )
//
//    } catch (exception: ApolloException) {
//      Timber.d("AddUser: name: ${exception.stackTraceToString()}")
//      Timber.d("AddUser: name: ${exception.message}")
//      throw HttpException()
//    }
    TODO()
  }

  override suspend fun authenticate(email: String, password: String): Credentials {
    return Credentials()
//    val response = apolloClient.mutate(AuthenticateUserMutation(email, password)).await()
//    Timber.d("Authenticate result ${response.data}")
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
//    val data = response.data?.tokens ?: throw HttpException(HTTP_NOT_FOUND)
//    return Credentials(
//      accessToken = data.access_token,
//    )
  }
}

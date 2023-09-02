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
@file:JvmName("ApolloGraphqlUtils")

package com.ericafenyo.bikediary.network

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException
import com.ericafenyo.bikediary.model.HttpException
import com.ericafenyo.libs.serialization.KotlinJsonSerializer
import org.json.JSONObject

fun toHttpException(exception: ApolloException): HttpException {
  val error: HttpException = when (exception) {
    is ApolloHttpException -> {
      val map = exception.error.extensions ?: mutableMapOf()
      val json = JSONObject(map).toString()
      KotlinJsonSerializer.getInstance().fromJson(json, ApolloErrorResponse.serializer()).run {
        HttpException(
          status = this.exception.status,
          message = this.exception.message
        )
      }
    }

    is UnexpectedNetworkException -> {
      HttpException(message = exception.message, status = 500)
    }

    else -> {
      HttpException(message = exception.message, status = 500)
    }
  }

  return error
}

/**
 * A shorthand to get a non-nullable data.
 */
suspend fun <T : Operation.Data> ApolloCall<T>.invoke(): T {
  return execute().run {
    if (hasErrors()) {
      throw errors?.firstOrNull()?.let { ApolloHttpException(it) }
        ?: UnexpectedNetworkException("The server did not return errors")
    } else {
      data ?: throw UnexpectedNetworkException("The server did not return any data")
    }
  }
}

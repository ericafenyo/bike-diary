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

package com.ericafenyo.libs.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
class KotlinJsonSerializer {
  val jsonInstance = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
  }

  fun <T> toJson(serializer: SerializationStrategy<T>, value: T): String {
    return jsonInstance.encodeToString(serializer, value)
  }

  inline fun <reified T> toJson(value: T): String {
    return jsonInstance.encodeToString(value)
  }

  inline fun <reified T> fromJson(deserializer: DeserializationStrategy<T>, json: String): T {
    return jsonInstance.decodeFromString(deserializer, json)
  }

  inline fun <reified T> fromJson(json: String): T {
    return jsonInstance.decodeFromString(json)
  }

  companion object {
    @Volatile
    private var INSTANCE: KotlinJsonSerializer? = null

    @JvmStatic
    fun getInstance(): KotlinJsonSerializer {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: KotlinJsonSerializer()
          .also { INSTANCE = it }
      }
    }
  }
}

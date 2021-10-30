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

import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
open class KotlinJsonSerializer {

  val serializerInstance = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
  }

  fun <T : Any> toJson(value: T, serializer: SerializationStrategy<T>): String {
    return serializerInstance.encodeToString(serializer, value)
  }

  fun toJson(value: Any, clazz: KClass<*>): String {
    val serializer = serializerInstance.serializersModule.serializer(clazz.java)
    return serializerInstance.encodeToString(serializer, value)
  }

  fun toJson(value: Any, type: Type): String {
    val serializer = serializerInstance.serializersModule.serializer(type)
    return serializerInstance.encodeToString(serializer, value)
  }

  inline fun <reified T> toJson(value: T): String {
    return serializerInstance.encodeToString(value)
  }

  inline fun <reified T> fromJson(deserializer: DeserializationStrategy<T>, json: String): T {
    return serializerInstance.decodeFromString(deserializer, json)
  }

  inline fun <reified T> fromJson(json: String): T {
    return serializerInstance.decodeFromString(json)
  }

  fun fromJson(json: String, clazz: KClass<*>): Any {
    val serializer = serializerInstance.serializersModule.serializer(clazz.java)
    return serializerInstance.decodeFromString(serializer, json)
  }

  fun fromJson(json: String, type: Type): Any {
    val serializer = serializerInstance.serializersModule.serializer(type)
    return serializerInstance.decodeFromString(serializer, json)
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

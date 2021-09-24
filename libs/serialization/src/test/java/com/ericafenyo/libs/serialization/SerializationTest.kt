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

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test


class SerializationTest {
  // Get a reference to the class under test
  private val serializer: KotlinJsonSerializer by lazy { KotlinJsonSerializer.getInstance() }

  @Serializable
  data class Person(
    val id: String = "6136c13543ff2226f41f0fd9",
    val name: String = "Killer Bean",
    val age: Int = 34,
    val gender: String = "unspecified",
  )

  // Control sample
  private val map = mapOf(
    "id" to JsonPrimitive("6136c13543ff2226f41f0fd9"),
    "name" to JsonPrimitive("Killer Bean"),
    "age" to JsonPrimitive(34),
    "gender" to JsonPrimitive("unspecified"),
  ).run { JsonObject(this) }

  @Test
  fun encodeToJson() {
    // Given a person object
    val person = Person()
    // When it is encoded to JSON
    val json = serializer.toJson(person)
    // Then the `json` should be equal to the control json
    assertThat(json).isEqualTo(map.toString())
  }

  @Test
  fun encodeToJson_with_deserializationStrategy() {
    // Given a person object
    val person = Person()
    // When it is encoded to JSON
    val json = serializer.toJson(person, Person.serializer())
    // Then the `json` should be equal to the control json
    assertThat(json).isEqualTo(map.toString())
  }

  @Test
  fun decodeFromJson() {
    // Given a json string
    val json = map.toString()
    // When an object is decoded from JSON
    val person = serializer.fromJson<Person>(json)
    // Then the `object`  should be equal to it's class instance
    assertThat(Person()).isEqualTo(person)
  }

  @Test
  fun decodeFromJson_with_serializationStrategy() {
    // Given a json string
    val json = map.toString()
    // When an object is decoded from JSON
    val person = serializer.fromJson(Person.serializer(), json)
    // Then the `object`  should be equal to it's class instance
    assertThat(Person()).isEqualTo(person)
  }

  @Test
  fun decodeFromJson_with_type() {
    // Given a json string
    val json = map.toString()
    // When an object is decoded from JSON
    val person = serializer.fromJson(json, Person::class)
    // Then the `object`  should be equal to it's class instance
    assertThat(Person()).isEqualTo(person)
  }
}

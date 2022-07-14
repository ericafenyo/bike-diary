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

package com.ericafenyo.tracker.database

import androidx.room.TypeConverter
import com.ericafenyo.tracker.database.analyzed.Trace
import com.ericafenyo.tracker.location.SimpleLocation
import com.ericafenyo.tracker.util.JsonSerializerManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class TracesConverter() {
  private val jsonSerializer = JsonSerializerManager.serializer()

  @TypeConverter
  fun fromTrace(value: List<Trace>): String {
    return jsonSerializer.encodeToString(value)
  }

  @TypeConverter
  fun toTrace(value: String): List<Trace> {
    return jsonSerializer.decodeFromString(value)
  }
}


class SimpleLocationConverter {
  private val json = JsonSerializerManager.serializer()

  @TypeConverter
  fun fromSimpleLocation(value: SimpleLocation): String {
    return json.encodeToString(SimpleLocation.serializer(), value)
  }

  @TypeConverter
  fun toSimpleLocation(value: String): SimpleLocation {
    return json.decodeFromString(SimpleLocation.serializer(), value)
  }
}
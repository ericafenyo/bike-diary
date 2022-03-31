/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 TransWay
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

package com.ericafenyo.bikediary.database

import androidx.room.TypeConverter
import com.ericafenyo.bikediary.model.Quests
import com.ericafenyo.libs.serialization.KotlinJsonSerializer
import org.json.JSONArray


object Converters {
  val serializer = KotlinJsonSerializer.getInstance()

  @TypeConverter
  fun fromStrings(points: String): List<String> {
    return listOf()
  }

  @TypeConverter
  fun toStrings(points: List<String>): String {
    return JSONArray().toString()
  }

  @TypeConverter
  fun fromQuests(quests: String): Quests {
    return serializer.fromJson(quests, Quests.serializer())
  }

  @TypeConverter
  fun toQuests(quests: Quests): String {
    return serializer.toJson(quests, Quests.serializer())
  }
}

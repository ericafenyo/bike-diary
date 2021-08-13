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

<<<<<<< HEAD:tracker/src/main/java/com/ericafenyo/tracker/datastore/Converters.kt
package com.ericafenyo.tracker.datastore
=======
package com.ericafenyo.bikediary.database.entity
>>>>>>> a0365d1 (Test :logger module build):database/src/main/java/com/ericafenyo/bikediary/database/entity/Record.kt

import androidx.room.TypeConverter
import com.ericafenyo.tracker.location.SimpleLocation
import com.ericafenyo.tracker.util.JsonSerializerManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class Converters {
  private val json = JsonSerializerManager.serializer()

  @TypeConverter
  fun fromSimpleLocation(value: SimpleLocation): String = json.encodeToString(value)

  @TypeConverter
  fun toSimpleLocation(value: String): SimpleLocation = json.decodeFromString(value)
}
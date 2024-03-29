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

package com.ericafenyo.tracker.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ericafenyo.tracker.data.model.SimpleLocation
import java.time.LocalDateTime
import java.util.TimeZone

/**
 * A Record is an entry containing location data with additional metadata.
 *
 * @property id an auto generated int for identifying a specific Record
 * @property writeTime the time in seconds at which the Record was added to the database
 * @property fmt a human readable time
 * @property timezone the devices current timezone. Example Europe/Paris
 * @property location a [SimpleLocation] object
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-01-30
 */
@Entity(tableName = "records")
data class Record(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val writeTime: Double,
  val fmt: String,
  val timezone: String,
  val location: SimpleLocation
) {
  companion object {
    fun fromSimpleLocation(location: SimpleLocation): Record = Record(
      writeTime = (System.currentTimeMillis() / 1000).toDouble(),
      fmt = LocalDateTime.now().toString(),
      timezone = TimeZone.getDefault().id,
      location = location
    )
  }
}

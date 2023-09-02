/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2023 Eric Afenyo
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

package com.ericafenyo.bikediary.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ericafenyo.bikediary.model.Coordinate
import java.time.Instant
import java.util.UUID

@Entity(tableName = "photos")
data class PhotoEntity(
  /**
   * Unique identifier for the photo
   */
  @PrimaryKey @ColumnInfo(name = "id") val id: UUID,

  /**
   * Contextual information associated with the photo.
   */
  @ColumnInfo(name = "caption") val caption: String,

  /**
   * Hash value of the photo's contents
   */
  @ColumnInfo(name = "hash") val hash: String,

  /**
   * Geographic coordinates or address of the place where the photo was taken.
   */
  @ColumnInfo(name = "coordinate") val coordinate: Coordinate,

  /**
   * The date and time when the photo was taken.
   */
  @ColumnInfo(name = "timestamp") val timestamp: Instant,
)

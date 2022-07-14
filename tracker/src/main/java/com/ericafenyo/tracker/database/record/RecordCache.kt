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

import android.content.Context
import com.ericafenyo.tracker.database.Cache
import com.ericafenyo.tracker.database.TrackerDatabase

/**
 * An implementation of the [Cache] interface responsible for
 * saving [Record] data into an SQLite database.
 *
 * @author Eric
 * created on 2021-01-25
 */
class RecordCache constructor(context: Context) : Cache<Record> {
  private val records = TrackerDatabase.getInstance(context).getRecords()

  override suspend fun put(entry: Record) {
    records.insert(entry)
  }

  override suspend fun entries(): List<Record> {
    return records.getAll()
  }

  override suspend fun clear() {
    records.clear()
  }

  companion object {
    @Volatile
    private var INSTANCE: Cache<Record>? = null

    /**
     * Use this to replace the current [RecordCache] with a custom implementation
     *
     * @param cache a custom [Cache] implementation
     */
    fun swap(cache: Cache<Record>?) {
      INSTANCE = cache
    }

    @JvmStatic
    fun getInstance(context: Context): Cache<Record> {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: RecordCache(context).also { INSTANCE = it }
      }
    }
  }
}

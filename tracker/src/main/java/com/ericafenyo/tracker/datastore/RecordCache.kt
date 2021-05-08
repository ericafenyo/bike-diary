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

package com.ericafenyo.tracker.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow

/**
 * An implementation of the [Cache] interface providing methods for performing
 * operations on [Record] objects.
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-01-25
 */
internal class RecordCache constructor(context: Context) : Cache<Record> {
  private val records = DataStore.getInstance(context).getRecords()

  override suspend fun put(value: Record) = records.insert(value)

  override suspend fun putMany(values: List<Record>) = records.bulkInsert(values)

  override suspend fun getAll(): List<Record> = records.getAll()

  override suspend fun getLatest(): Record = records.getLatest()

  override suspend fun streams(): Flow<List<Record>> = records.streams()

  override suspend fun single(): Flow<Record> = records.single()

  override fun clear() = records.clear()

  companion object {
    @Volatile
    private var INSTANCE: Cache<Record>? = null

    /**
     * Use this to replace the current [RecordCache] with a custom implementation
     * with
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

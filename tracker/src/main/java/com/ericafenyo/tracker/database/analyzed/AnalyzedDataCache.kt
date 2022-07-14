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

package com.ericafenyo.tracker.database.analyzed

import android.content.Context
import com.ericafenyo.tracker.database.Cache
import com.ericafenyo.tracker.database.TrackerDatabase

class AnalyzedDataCache(context: Context) : Cache<AnalyzedData> {
  private val dao = TrackerDatabase.getInstance(context).getAnalyzedDataDao()

  override suspend fun put(entry: AnalyzedData) {
    dao.insert(entry)
  }

  override suspend fun entries(): List<AnalyzedData> {
    return dao.getAll()
  }

  override suspend fun clear() {
    dao.clear()
  }

  companion object {
    @Volatile
    private var INSTANCE: AnalyzedDataCache? = null

    @JvmStatic
    fun getInstance(context: Context): AnalyzedDataCache {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: AnalyzedDataCache(context)
          .also { INSTANCE = it }
      }
    }
  }
}
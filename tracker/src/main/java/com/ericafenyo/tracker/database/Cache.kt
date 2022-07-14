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


/**
 * An abstract object that defines the methods for saving and retrieving cached data.
 *
 * @param T the type of data to be saved.
 *
 * @author Eric
 * @since 1.0
 *
 * @see [com.ericafenyo.tracker.database.record.RecordCache]
 * @see [com.ericafenyo.tracker.database.analyzed.AnalyzedDataCache]
 *
 * created on 2021-01-30
 */
interface Cache<T : Any> {
  /**
   * Add the given entry into the cache.
   *
   * @param entry the entry to be saved
   */
  suspend fun put(entry: T)

  /**
   * Returns a list of all entries in the cache.
   */
  suspend fun entries(): List<T>

  /**
   * Removes all elements from the cache.
   */
  suspend fun clear()
}

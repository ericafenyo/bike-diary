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

package com.ericafenyo.bikediary.repositories.lastrequest

import com.ericafenyo.bikediary.database.dao.LastRequestDao
import com.ericafenyo.bikediary.database.entity.LastRequest
import com.ericafenyo.bikediary.database.entity.Request
import java.time.Instant
import java.time.temporal.TemporalAmount

class LastRequestStore(
  private val request: Request,
  private val dao: LastRequestDao,
) {

  suspend fun updateLastRequest(timestamp: Long = Instant.now().toEpochMilli()) {
    dao.insert(LastRequest(request = request, timestamp = timestamp))
  }

  suspend fun invalidateLastRequest() = updateLastRequest(Instant.EPOCH.toEpochMilli())

  suspend fun isRequestExpired(threshold: TemporalAmount): Boolean {
    val definedPastInstant = Instant.now().minus(threshold)
    val lastRequestInstant = getLastRequestInstant()
    return lastRequestInstant.isBefore(definedPastInstant)
  }

  private suspend fun getLastRequestInstant(): Instant {
    return dao.getLastRequest(request)?.let {
      Instant.ofEpochMilli(it.timestamp)
    } ?: Instant.EPOCH
  }
}

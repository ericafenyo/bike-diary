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

package com.ericafenyo.tracker.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ericafenyo.bikediary.logger.Logger
import com.ericafenyo.tracker.data.model.simplify
import com.ericafenyo.tracker.database.record.Record
import com.ericafenyo.tracker.database.record.RecordCache
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Receiver for broadcasts sent by [LocationUpdatesAction] through pending intent.
 */
class LocationUpdatesReceiver : BroadcastReceiver() {
  @Suppress("PrivatePropertyName") private val TAG = "LocationUpdatesReceiver"

  override fun onReceive(context: Context, intent: Intent) {
    Logger.debug(context, TAG, "onReceive(context: $context, intent: $intent)")

    val locations = if (LocationResult.hasResult(intent)) {
      Logger.debug(context, TAG, "intent has location results, extracting it")
      LocationResult.extractResult(intent)?.locations
    } else {
      Logger.debug(context, TAG, "intent has not location results, returning null")
      null
    }

    // Skip if we don't have a location object
    if (locations.isNullOrEmpty()) {
      Logger.debug(context, TAG, "Location value is null, skipping")
      return
    }

    val location = locations.first().simplify()
    runBlocking(Dispatchers.IO) {
      RecordCache.getInstance(context).put(Record.fromSimpleLocation(location))
    }
  }
}

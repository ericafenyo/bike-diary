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

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.ericafenyo.bikediary.logger.Logger
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

/**
 * This class is responsible for starting and stopping a location update.
 *
 * @author Eric
 */

class LocationUpdatesAction(private val context: Context) {
  @Suppress("PrivatePropertyName") private val TAG = "LocationUpdatesAction"
  private val client = LocationServices.getFusedLocationProviderClient(context)

  fun start(): Task<Void>? {
    try {
      return client.requestLocationUpdates(locationRequest, getPendingIntent())
    } catch (exception: SecurityException) {
      Logger.error(
        context,
        TAG,
        "Security error: ${exception.message} while starting location updates"
      )
      return null
    }
  }

  private val locationRequest: LocationRequest = LocationRequest.create()
    .setInterval(FIVE_SECONDS)
    .setSmallestDisplacement(10F) // In meters
    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

  fun stop(): Task<Void>? = client.removeLocationUpdates(getPendingIntent())

  private fun getPendingIntent(): PendingIntent {
    val intent = Intent(context, LocationUpdatesReceiver::class.java)
    return PendingIntent.getBroadcast(context, 0, intent, pendingIntentFlag)
  }

  /**
   * FLAG_UPDATE_CURRENT replaces any existing broadcast.
   * We want to handle one location update at a time.
   */
  private val pendingIntentFlag = if (VERSION.SDK_INT >= VERSION_CODES.S) {
    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
  } else {
    PendingIntent.FLAG_UPDATE_CURRENT
  }

  companion object {
    const val FIVE_SECONDS: Long = 5 * 1000
  }
}

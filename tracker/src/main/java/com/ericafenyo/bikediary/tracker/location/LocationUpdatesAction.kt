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

package com.ericafenyo.bikediary.tracker.location

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Looper
import com.ericafenyo.bikediary.tracker.logger.Logger
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

/**
 * This class contains methods for starting and stopping a location update.
 */
class LocationUpdatesAction(private val context: Context) {
  private val tag = "LocationUpdatesAction"
  private var locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult?) {
      Logger.debug(context, tag, "locationResult: $locationResult")
    }
  }

  fun start(): Task<Void>? {
    return try {
      LocationServices.getFusedLocationProviderClient(context)
        .requestLocationUpdates(locationRequest, getPendingIntent())
    } catch (exception: SecurityException) {
      Logger.error(
        context,
        tag,
        "Security error: ${exception.message} while starting location updates"
      )
      null
    }
  }

  private val locationRequest: LocationRequest = LocationRequest.create()
    .setInterval(TEN_SECONDS)
    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

  fun stop(): Task<Void>? = LocationServices.getFusedLocationProviderClient(context)
    .removeLocationUpdates(getPendingIntent())

  private fun getPendingIntent(): PendingIntent {
    val intent = Intent(context, LocationUpdatesReceiver::class.java)
    // FLAG_UPDATE_CURRENT replaces any existing broadcast.
    // We want to handle one location update at a time.
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  companion object {
    const val THIRTY_SECONDS: Long = 30 * 1000
    const val TEN_SECONDS: Long = 10 * 1000 // For debug purpose
  }
}
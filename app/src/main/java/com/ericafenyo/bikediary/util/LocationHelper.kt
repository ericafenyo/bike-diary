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

package com.ericafenyo.bikediary.util

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import timber.log.Timber

object LocationHelper {
  fun getCurrentLocation(context: Context, block: (Location?) -> Unit) {
    try {
      LocationServices.getFusedLocationProviderClient(context)
        .requestLocationUpdates(
          getLocationRequest(),
          getLocationCallback(block),
          Looper.getMainLooper()
        )
      LocationServices.getFusedLocationProviderClient(context)
        .removeLocationUpdates(getLocationCallback(block))
    } catch (exception: SecurityException) {
      Timber.e(exception, "Security error: ${exception.message} while starting location updates")
    }
  }

  private fun getLocationCallback(block: (Location?) -> Unit): LocationCallback {
    return object : LocationCallback() {
      override fun onLocationResult(result: LocationResult) {
        block(result.lastLocation)
      }
    }
  }

  private fun getLocationRequest(): LocationRequest = LocationRequest.create()
    .setNumUpdates(1)
    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    .setInterval(0)
    .setExpirationDuration(5_000)
    .setFastestInterval(0)
}

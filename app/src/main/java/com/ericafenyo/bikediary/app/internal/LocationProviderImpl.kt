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

package com.ericafenyo.bikediary.app.internal

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.ericafenyo.bikediary.app.LocationProvider
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

@Singleton
class LocationProviderImpl @Inject constructor(
  @ApplicationContext private val context: Context
) : LocationProvider {

  @SuppressLint("MissingPermission")
  override suspend fun getCurrentLocation(): Location =
    suspendCancellableCoroutine { continuation ->
      val services = LocationServices.getFusedLocationProviderClient(context)
      val request = CurrentLocationRequest.Builder()
        .setGranularity(Granularity.GRANULARITY_FINE)
        .setMaxUpdateAgeMillis(0)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

      services.getCurrentLocation(request, null)
        .addOnSuccessListener { location ->
          Timber.tag("DEBUGGING_LOG").i("This is the photo location: $location")
          continuation.resume(location)
        }.addOnFailureListener { exception ->
          Timber.tag("DEBUGGING_LOG").e(exception, "This is the failed photo location:")
          continuation.resumeWithException(exception)
        }
    }
}
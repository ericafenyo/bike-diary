/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Eric Afenyo
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

package com.ericafenyo.bikediary

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.ericafenyo.bikediary.util.Notifications
import com.ericafenyo.tracker.Tracker
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mapbox.maps.ResourceOptionsManager
import dagger.hilt.android.HiltAndroidApp
import io.sentry.android.core.SentryAndroid
import io.sentry.android.timber.SentryTimberIntegration
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class App : Application(), Configuration.Provider {
  @Inject lateinit var workerFactory: HiltWorkerFactory

  override fun onCreate() {
    super.onCreate()
    // Initialize the timezone information android three ten
    AndroidThreeTen.init(this)
    // Setup mapbox sdk
    ResourceOptionsManager.getDefault(applicationContext, BuildConfig.MAPBOX_PUBLIC_TOKEN)
    // Initialize sentry logger service
    SentryAndroid.init(this) { options ->
      // Send timber error logs
      options.environment = getString(R.string.sentry_environment)
      options.dsn = BuildConfig.SENTRY_DSN
      options.addIntegration(SentryTimberIntegration())
    }

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    Tracker.initialize(this)
  }

  override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
    .setWorkerFactory(workerFactory)
    .build()
}

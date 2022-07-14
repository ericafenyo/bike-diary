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

package com.ericafenyo.bikediary.app.worker.initializers

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.ericafenyo.bikediary.app.worker.SynchronizationWorker

object Synchronizer {
  fun initialize(context: Context) {
    AppInitializer.getInstance(context)
      .initializeComponent(SynchronizerInitializer::class.java)
  }
}

// This name should not be changed
private const val workerName = "com.ericafenyo.bikediary.app.worker.SYNCHRONIZATION_WORKER_NAME"

class SynchronizerInitializer : Initializer<Synchronizer> {
  override fun create(context: Context): Synchronizer {
    WorkManager.getInstance(context).apply {
      // Run sync on app startup and ensure only one sync worker runs at any time
      enqueueUniqueWork(workerName, ExistingWorkPolicy.REPLACE, SynchronizationWorker.start())
    }

    return Synchronizer
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
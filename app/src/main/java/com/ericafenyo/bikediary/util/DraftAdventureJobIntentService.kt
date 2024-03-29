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
import android.content.Intent
import androidx.core.app.JobIntentService
import com.ericafenyo.bikediary.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
@AndroidEntryPoint
class DraftAdventureJobIntentService : JobIntentService() {

  override fun onHandleWork(intent: Intent) {
    Logger.debug(applicationContext, TAG, "onHandleWork(intent: $intent)")
    runBlocking { }
  }

  override fun onDestroy() {
    super.onDestroy()
    Logger.debug(applicationContext, TAG, "onDestroy() All work complete")
  }

  companion object {
    private const val TAG = "DraftAdventureJobIntentService"

    /**
     * Unique job ID for this service.
     */
    const val JOB_ID = 2020

    /**
     * Convenience method for enqueuing work in to this service.
     */
    fun enqueueWork(context: Context, work: Intent) {
      enqueueWork(context, DraftAdventureJobIntentService::class.java, JOB_ID, work)
    }

    fun getStartIntent(packageContext: Context): Intent {
      return Intent(packageContext, DraftAdventureJobIntentService::class.java)
    }
  }
}
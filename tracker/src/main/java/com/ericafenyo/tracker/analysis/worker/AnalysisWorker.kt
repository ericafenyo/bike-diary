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

package com.ericafenyo.tracker.analysis.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.ericafenyo.bikediary.di.qualifier.Dispatcher
import com.ericafenyo.bikediary.di.qualifier.DispatcherType.IO
import com.ericafenyo.bikediary.logger.Logger
import com.ericafenyo.bikediary.network.analysis.AnalysisService
import com.ericafenyo.tracker.analysis.Analyser
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class AnalysisWorker @AssistedInject constructor(
  @Assisted private val context: Context,
  @Assisted params: WorkerParameters,
  @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
  private val service: AnalysisService,
  private val analyser: Analyser,
) : CoroutineWorker(context, params) {
  override suspend fun doWork(): Result = withContext(ioDispatcher) {
    Logger.debug(context, TAG, "doWork()")

    val successful = analyser.startAnalysis()

    if (successful) {
      val data = analyser.getAnalysedAdventures()
      val json = Gson().toJson(data)

      val request = Test.parse(json)
      Timber.d("The graphql request: $request")
      service.synchronize(request)
    }

    Result.success()
  }

  companion object {
    private const val TAG: String = "AnalysisWorker"

    val request: WorkRequest = OneTimeWorkRequestBuilder<AnalysisWorker>().build()
  }
}

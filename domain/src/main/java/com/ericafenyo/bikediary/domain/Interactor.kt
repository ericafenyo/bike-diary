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

package com.ericafenyo.bikediary.domain


import com.ericafenyo.tracker.data.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Executes business logic synchronously or asynchronously using Coroutines.
 */
abstract class Interactor<R>(private val coroutineDispatcher: CoroutineDispatcher) {
  /** Executes the use case asynchronously and returns a [Result].
   *
   * @return a [Result].
   */
  suspend operator fun invoke(): Result<R> {
    return try {
      // Moving all use case's executions to the injected dispatcher
      // In production code, this is usually the Default dispatcher (background thread)
      // In tests, this becomes a TestCoroutineDispatcher
      withContext(coroutineDispatcher) {
        execute().let {
          Result.Success(it)
        }
      }
    } catch (exception: Exception) {
      Timber.e(exception)
      Result.Error(exception)
    }
  }

  /**
   * Override this to set the code to be executed.
   */
  @Throws(RuntimeException::class)
  protected abstract suspend fun execute(): R
}

abstract class SubjectInteractor<P, R> {
  private val sharedFlow = MutableSharedFlow<P>(
    replay = 1,
    extraBufferCapacity = 1,
    onBufferOverflow = DROP_OLDEST
  )

  operator fun invoke(params: P) {
    sharedFlow.tryEmit(params)
  }

  protected abstract fun execute(params: P): Flow<R>

  @OptIn(ExperimentalCoroutinesApi::class)
  fun asFlow(): Flow<R> = sharedFlow.flatMapLatest { params -> execute(params) }
}

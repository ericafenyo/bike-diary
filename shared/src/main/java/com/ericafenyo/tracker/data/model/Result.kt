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

package com.ericafenyo.tracker.data.model

import com.ericafenyo.bikediary.shared.SimpleResult
import com.ericafenyo.tracker.data.model.Result.Error
import com.ericafenyo.tracker.data.model.Result.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

  data class Success<out T>(val data: T) : Result<T>()
  data class Error(val exception: Exception) : Result<Nothing>()
  object Loading : Result<Nothing>()

  override fun toString(): String {
    return when (this) {
      is Success<*> -> "Success[data=$data]"
      is Error -> "Error[exception=$exception]"
      Loading -> "Loading"
    }
  }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
  get() = this is Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
  return (this as? Success<T>)?.data ?: fallback
}

fun <T> Result<T>.getOrThrow(): T {
  return when (this) {
    is Success -> this.data
    is Error -> throw this.exception
    else -> throw UnsupportedOperationException("Result can only of type Success or Error")
  }
}

fun <T> Result<T>.getOrElse(block: () -> T): T {
  return (this as? Success<T>)?.data ?: block()
}

val <T> Result<T>.data: T?
  get() = (this as? Success)?.data

fun <T> Result<T>.simplify(): SimpleResult<T> {
  return when (this) {
    is Success -> SimpleResult.Success(data)
    is Error -> SimpleResult.Error(exception)
    else -> throw UnsupportedOperationException("Result can only of type Success or Error")
  }
}

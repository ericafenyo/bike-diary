/*
 * Copyright (C) 2020 Transway
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ericafenyo.bikediary.shared

import com.ericafenyo.bikediary.shared.SimpleResult.Error
import com.ericafenyo.bikediary.shared.SimpleResult.Success


/**
 * A object with the necessary information for handling UI states.
 * For example, if an API request is loading or failed.
 *
 * @param <T> the result type
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-08-24
 */

sealed class SimpleResult<out R> {

  data class Success<out T>(val data: T) : SimpleResult<T>()
  data class Error(val exception: Exception) : SimpleResult<Nothing>()

  override fun toString(): String {
    return when (this) {
      is Success<*> -> "Success[data=$data]"
      is Error -> "Error[exception=$exception]"
    }
  }
}

/**
 * `true` if [SimpleResult] is of type [Success] & holds non-null [Success.data].
 */
val SimpleResult<*>.succeeded
  get() = this is Success && data != null

fun <T> SimpleResult<T>.successOr(fallback: T): T {
  return (this as? Success<T>)?.data ?: fallback
}

fun <T> SimpleResult<T>.getOrThrow(): T {
  return when (this) {
    is Success -> this.data
    is Error -> throw this.exception
  }
}

fun <T> SimpleResult<T>.throwOnError() {
  if (this is Error) {
    throw this.exception
  }
}

fun <T> SimpleResult<T>.getOrElse(block: () -> T): T {
  return (this as? Success<T>)?.data ?: block()
}

val <T> SimpleResult<T>.data: T?
  get() = (this as? Success)?.data

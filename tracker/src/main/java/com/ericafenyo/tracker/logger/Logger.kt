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

package com.ericafenyo.tracker.logger

import android.content.Context
import android.util.Log
import com.ericafenyo.tracker.datastore.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.PrintWriter
import java.io.StringWriter

object Logger {
  private fun database(context: Context) = DataStore.getInstance(context).logs()
  private val IOContext = Dispatchers.IO

  fun clear(context: Context) = runBlocking(IOContext) { database(context).clear() }

  fun log(context: Context, level: String, tag: String, message: String) = runBlocking(IOContext) {
    database(context).insert(LogEntity(level = level, message = "$tag, $message"))
  }

  fun debug(context: Context, tag: String, message: String) {
    log(context, "DEBUG", tag, message)
    Log.d(tag, message)
  }

  fun info(context: Context, tag: String, message: String) {
    log(context, "INFO", tag, message)
    Log.i(tag, message)
  }

  fun warn(context: Context, tag: String, message: String) {
    log(context, "WARN", tag, message)
    Log.w(tag, message)
  }

  fun error(context: Context, tag: String, message: String) {
    log(context, "ERROR", tag, message)
    Log.e(tag, message)
  }

  fun exception(context: Context, tag: String, exception: Exception) {
    try {
      val stringWriter = StringWriter()
      val printWriter = PrintWriter(stringWriter)
      exception.printStackTrace(printWriter)
      error(context, tag, printWriter.toString())
      Log.e(tag, printWriter.toString())
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}

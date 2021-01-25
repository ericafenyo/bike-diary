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

package com.ericafenyo.tracker.database

import android.content.Context
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*

class RecordCache private constructor(context: Context) : Cache {
  private val records = DataStore.getInstance(context).getRecords()

  override fun putSensorData(key: String, value: Any) {
    createRecord(SENSOR_DATA_TYPE, key, value).also { records.save(it) }
  }

  override fun putMessage(key: String, value: Any) {
    createRecord(MESSAGE_TYPE, key, value).also { records.save(it) }
  }

  override fun putDocument(key: String, value: Any) {
    createRecord(DOCUMENT_TYPE, key, value).also { records.save(it) }
  }

  override fun getSensorData(keys: List<String>): List<*> {
    return runBlocking(Dispatchers.IO) {
       records.read(keys)
    }
  }

  override fun clear() {
    records.clear()
  }

  @WorkerThread
  private fun createRecord(type: String, key: String, data: Any) = Record(
    ts = (System.currentTimeMillis() / 1000).toDouble(),
    timezone = TimeZone.getDefault().id,
    type = type,
    key = key,
    data = Gson().toJson(data)
  )

  companion object {
    private const val SENSOR_DATA_TYPE = "sensor-data"
    private const val MESSAGE_TYPE = "message"
    private const val DOCUMENT_TYPE = "document"

    const val KEY_LOCATION = "background/location"
    const val KEY_TRIP_STARTED = "action/start"
    const val KEY_TRIP_STOPPED = "action/stop"

    @Volatile
    private var INSTANCE: RecordCache? = null

    @JvmStatic
    fun getInstance(context: Context): Cache {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: RecordCache(context)
          .also { INSTANCE = it }
      }
    }
  }
}

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
import android.util.Log
import androidx.annotation.WorkerThread
import com.ericafenyo.tracker.logger.Logger
import com.google.gson.Gson
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * An implementation of the [Cache] interface providing methods for performing
 * operations on [Record] objects.
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-01-25
 */
class RecordCache private constructor(private val context: Context) : Cache<Record> {
  private val records = DataStore.getInstance(context).getRecords()

  override fun putSensorData(key: String, value: Any) {
    createRecord(SENSOR_DATA_TYPE, key, value).also { records.insert(it) }
  }

  override fun getSensorData(keys: List<String>): List<Record> {
    return runBlocking(Dispatchers.IO) {
      records.read(keys)
    }
  }

  override fun putMessage(key: String, value: Any) {
    createRecord(MESSAGE_TYPE, key, value).also { records.insert(it) }
  }

  override fun putDocuments(key: String, values: List<Any>) {
    Logger.debug(context, TAG, "Adding document with key: $key")
    val recodeList = values.map { value -> createRecord(DOCUMENT_TYPE, key, value) }
    Log.d(TAG, "putDocuments: $recodeList")
    records.bulkInsert(recodeList)
  }

  override fun getDocuments(keys: List<String>): Flow<List<Record>> = flow {
    emitAll(records.observeRecords(keys))
  }

  override fun clear() {
    records.clear()
  }

  override fun clear(keys: List<String>) {
    records.clear(keys)
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
    private const val TAG = "RecordCache"

    private const val SENSOR_DATA_TYPE = "sensor-data"
    private const val MESSAGE_TYPE = "message"
    private const val DOCUMENT_TYPE = "document"

    const val KEY_LOCATION = "background/location"
    const val KEY_TRIP_STARTED = "action/start"
    const val KEY_TRIP_STOPPED = "action/stop"
    const val KEY_COLLECTION = "cache/trip"

    @Volatile
    private var INSTANCE: RecordCache? = null

    @JvmStatic
    fun getInstance(context: Context): Cache<Record> {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: RecordCache(context)
          .also { INSTANCE = it }
      }
    }
  }
}

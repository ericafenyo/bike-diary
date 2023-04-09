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

package com.ericafenyo.tracker

import android.app.Application
import android.app.Notification
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ericafenyo.libs.serialization.KotlinJsonSerializer
import com.ericafenyo.tracker.R.string
import com.ericafenyo.tracker.util.ExplicitIntent
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

class Tracker private constructor(val options: TrackerOptions) {

  companion object {
    private val currentOptions: ThreadLocal<TrackerOptions> = ThreadLocal<TrackerOptions>()

    private fun getTrackerOptions(): TrackerOptions {
      var options = currentOptions.get()

      if (options == null) {
        options = TrackerOptions()
        currentOptions.set(options)
      }

      return options
    }

    fun init(application: Application, optionsConfiguration: (TrackerOptions) -> TrackerOptions) {
      val defaultOptions = TrackerOptions()
      currentOptions.set(optionsConfiguration(defaultOptions))
    }

    @Volatile
    private var INSTANCE: Tracker? = null

    @JvmStatic
    fun getInstance(): Tracker {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Tracker(getTrackerOptions())
          .also { INSTANCE = it }
      }
    }
  }

  val state: Flow<State> = TODO()

  suspend fun start() {
//    application.sendBroadcast(ExplicitIntent(context, string.tracker_action_start))
  }

  suspend fun pause() {
    TODO("Not implemented")
  }

  suspend fun resume() {
    TODO("Not implemented")
  }

  suspend fun stop() {
    TODO("Not implemented")
  }

  class Storage private constructor(context: Context) {
    companion object {
      @Volatile
      private var INSTANCE: Storage? = null

      @JvmStatic
      fun getInstance(context: Context): Storage {
        return INSTANCE ?: synchronized(this) {
          INSTANCE ?: Storage(context)
            .also { INSTANCE = it }
        }
      }
    }

    private val Context.dataStore by preferencesDataStore(name = "tracker_data_storage")
    private val dataStore = context.dataStore

    /**
     * Store a given value in the Storage.
     *
     * @param key  the key of the value to store.
     * @param value the value to store.
     */
    suspend fun <T : Any> store(key: String, value: T) {
      val json = KotlinJsonSerializer.getInstance().toJson(value, value::class)
      dataStore.edit { preference -> preference[stringPreferencesKey(key)] = json }
    }


    /**
     * Retrieve a value from the Storage.
     *
     * @param key the key of the value to retrieve.
     * @return the value that was previously saved.
     */
    fun <T : Any> retrieve(key: String, clazz: KClass<T>): Flow<T?> {
      TODO()
    }


    /**
     * Removes a value from the storage.
     *
     * @param key the key of the value to remove.
     */
    suspend fun <T : Any> remove(key: String, clazz: KClass<T>) {
      val preferenceKey = when (clazz) {
        Int::class -> intPreferencesKey(key)
        Long::class -> intPreferencesKey(key)
        String::class -> intPreferencesKey(key)
        Boolean::class -> intPreferencesKey(key)
        else -> throw UnsupportedOperationException("Key type $clazz not supported")
      }
      dataStore.edit { preferences -> preferences.remove(preferenceKey) }
    }

    /**
     * Removes all value from the storage.
     */
    suspend fun clear() {
      dataStore.edit { preferences -> preferences.clear() }
    }
  }

  enum class State { IDLE, READY, ONGOING, DISABLED }
}

class TrackerOptions {
  private var _notification: Notification? = null
  val notification: Notification? = _notification

  fun setNotification(notification: Notification): TrackerOptions {
    this._notification = notification
    return this
  }

}

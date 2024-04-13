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

import android.app.Notification
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ericafenyo.tracker.Tracker.State.IDLE
import com.ericafenyo.tracker.util.ExplicitIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class Tracker private constructor(private val context: Context) {
  private val storage = Storage.getInstance(context.applicationContext)

  val state: Flow<State> = storage.retrieve(TRACKER_CURRENT_STATE_KEY)
    .map { value ->
      value?.let { State.valueOf(it) } ?: IDLE
    }

  fun getState(): State = runBlocking { state.first() }

  suspend fun updateState(state: State): State {
    storage.store(TRACKER_CURRENT_STATE_KEY, "$state")
    return getState()
  }

  fun start() {
    Timber.d("Tracker start() called. Current state = ${getState()}")
    context.sendBroadcast(ExplicitIntent(context, R.string.tracker_action_start))
  }

  fun pause() {
    Timber.d("Tracker pause() called. Current state = ${getState()}")
    context.sendBroadcast(ExplicitIntent(context, R.string.tracker_action_pause))
  }

  fun resume() {
    Timber.d("Tracker resume() called. Current state = ${getState()}")
    context.sendBroadcast(ExplicitIntent(context, R.string.tracker_action_resume))
  }

  fun stop() {
    Timber.d("Tracker stop() called. Current state = ${getState()}")
    context.sendBroadcast(ExplicitIntent(context, R.string.tracker_action_stop))
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
    suspend fun store(key: String, value: String) {
      dataStore.edit { preference -> preference[stringPreferencesKey(key)] = value }
    }

    /**
     * Retrieve a string value from the Storage.
     *
     * @param key the key of the value to retrieve.
     * @return the value that was previously saved.
     */
    fun retrieve(key: String): Flow<String?> {
      return dataStore.data.map { preference -> preference[stringPreferencesKey(key)] }
    }

    /**
     * Removes a value from the storage.
     *
     * @param key the key of the value to remove.
     */
    suspend fun remove(key: String) {
      dataStore.edit { preferences -> preferences.remove(stringPreferencesKey(key)) }
    }

    /**
     * Removes all value from the storage.
     */
    suspend fun clear() {
      dataStore.edit { preferences -> preferences.clear() }
    }
  }


  enum class State { IDLE, ONGOING, PAUSE }

  companion object {
    const val TRACKER_CURRENT_STATE_KEY = "com.ericafenyo.tracker.TRACKER_CURRENT_STATE"

    private var tracker: Tracker? = null

    fun initialize(context: Context) {
      tracker = Tracker(context)
    }

    @Volatile
    private var INSTANCE: Tracker? = null

    @JvmStatic
    fun getInstance(): Tracker {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: currentTracker
          .also { INSTANCE = it }
      }
    }

    private val currentTracker: Tracker
      get() {
        return tracker ?: throw RuntimeException()
      }
  }

  data class Configuration(val notification: Notification)
}


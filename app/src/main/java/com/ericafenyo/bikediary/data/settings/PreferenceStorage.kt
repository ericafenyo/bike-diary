/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Eric Afenyo
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

package com.ericafenyo.bikediary.data.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.ericafenyo.bikediary.model.Theme
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
  var selectedTheme: String
  var observableSelectedTheme: Flow<String>
}

@OptIn(ObsoleteCoroutinesApi::class)
class SharedPreferenceStorage(context: Context) : PreferenceStorage {
  private val selectedThemeChannel: ConflatedBroadcastChannel<String> by lazy {
    ConflatedBroadcastChannel<String>().also { channel ->
      channel.trySend(selectedTheme).isSuccess
    }
  }

  private val prefs: Lazy<SharedPreferences> = lazy { // Lazy to prevent IO access to main thread.
    context.applicationContext.getSharedPreferences(
      PREFS_NAME, Context.MODE_PRIVATE
    ).apply {
      registerOnSharedPreferenceChangeListener(changeListener)
    }
  }

  private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
    when (key) {
      PREF_DARK_MODE_ENABLED -> selectedThemeChannel.trySend(selectedTheme).isSuccess
    }
  }

  override var selectedTheme: String by StringPreference(
    prefs, PREF_DARK_MODE_ENABLED, Theme.DARK.storageKey
  )

  override var observableSelectedTheme: Flow<String>
    get() = throw Error()
    set(_) = throw IllegalAccessException("This property can't be changed")

  companion object {
    const val PREFS_NAME = "com.ericafenyo.preference.HABIT_DIARY"

    const val PREF_DARK_MODE_ENABLED = "pref_dark_mode"
  }
}

class StringPreference(
  private val preferences: Lazy<SharedPreferences>,
  private val key: String,
  private val defaultValue: String
) : ReadWriteProperty<Any, String?> {

  @WorkerThread
  override fun getValue(thisRef: Any, property: KProperty<*>): String {
    return preferences.value.getString(key, defaultValue) ?: defaultValue
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
    preferences.value.edit { putString(key, value) }
  }
}

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

package com.ericafenyo.libs.storage

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ericafenyo.libs.serialization.KotlinJsonSerializer
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferenceStorage : FlowStorage

class PreferenceStorageImpl private constructor(context: Context) : PreferenceStorage {
  private val Context.dataStore by preferencesDataStore(name = "app_data_store")
  private val dataStore = context.dataStore

  override suspend fun store(key: String, value: Long) {
    putValue(longPreferencesKey(key), value)
  }

  override suspend fun store(key: String, value: Int) {
    putValue(intPreferencesKey(key), value)
  }

  override suspend fun store(key: String, value: String) {
    putValue(stringPreferencesKey(key), value)
  }

  override suspend fun store(key: String, value: Boolean) {
    putValue(booleanPreferencesKey(key), value)
  }

  override suspend fun <T : Any> store(key: String, value: T) {
    val json = KotlinJsonSerializer.getInstance().run { toJson(value, value.javaClass) }
    store(key, json)
  }

  override fun retrieveLong(key: String): Flow<Long?> {
    return getValue(longPreferencesKey(key))
  }

  override fun retrieveString(key: String): Flow<String?> {
    return getValue(stringPreferencesKey(key))
  }

  override fun retrieveInteger(key: String): Flow<Int?> {
    return getValue(intPreferencesKey(key))
  }

  override fun retrieveBoolean(key: String): Flow<Boolean?> {
    return getValue(booleanPreferencesKey(key))
  }

  override fun <T : Any> retrieve(key: String, clazz: KClass<T>): Flow<T?> {
    val serializer = KotlinJsonSerializer.getInstance()
    return retrieveString(key).map { value ->
      @Suppress("UNCHECKED_CAST")
      value?.let { serializer.fromJson(value, clazz) as T }
    }
  }

  override suspend fun <T : Any> remove(key: String, clazz: KClass<T>) {
    val preferenceKey = when (clazz) {
      Int::class -> intPreferencesKey(key)
      Long::class -> intPreferencesKey(key)
      String::class -> intPreferencesKey(key)
      Boolean::class -> intPreferencesKey(key)
      else -> throw UnsupportedOperationException("Key type $clazz not supported")
    }
    dataStore.edit { preferences -> preferences.remove(preferenceKey) }
  }

  override suspend fun clear() {
    dataStore.edit { preferences -> preferences.clear() }
  }

  private inline fun <reified T> getValue(key: Preferences.Key<T>): Flow<T?> {
    return dataStore.data.map { preference -> preference[key] }
  }

  private suspend inline fun <reified T : Any> putValue(key: Preferences.Key<T>, value: T) {
    dataStore.edit { preference -> preference[key] = value }
  }

  companion object {
    @Volatile
    private var INSTANCE: PreferenceStorageImpl? = null

    @JvmStatic
    fun getInstance(context: Context): PreferenceStorageImpl {

      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: PreferenceStorageImpl(context)
          .also { INSTANCE = it }
      }
    }
  }
}

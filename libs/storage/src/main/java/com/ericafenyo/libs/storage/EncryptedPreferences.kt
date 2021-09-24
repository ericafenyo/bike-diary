/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 TransWay
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
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme
import androidx.security.crypto.MasterKey.Builder
import androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM
import com.ericafenyo.libs.serialization.KotlinJsonSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

interface EncryptedPreferences : Storage

@Singleton
internal class EncryptedPreferencesImpl @Inject constructor(
  @ApplicationContext private val context: Context,
) : EncryptedPreferences {

  private val masterKey = Builder(context, ENCRYPTED_PREFERENCES_KEY_ALIAS)
    .setKeyScheme(AES256_GCM)
    .build()

  private val preferences: SharedPreferences by lazy {
    EncryptedSharedPreferences.create(
      context, ENCRYPTED_PREFERENCES_FILE_NAME, masterKey,
      AES256_SIV,
      PrefValueEncryptionScheme.AES256_GCM
    )
  }

  override fun store(key: String, value: Long) {
    preferences.edit { putLong(key, value) }
  }

  override fun store(key: String, value: Int) {
    preferences.edit { putInt(key, value) }
  }

  override fun store(key: String, value: String) {
    preferences.edit { putString(key, value) }
  }

  override fun store(key: String, value: Boolean) {
    preferences.edit { putBoolean(key, value) }
  }

  override fun <T : Any> store(key: String, value: T) {
    val serializer = KotlinJsonSerializer.getInstance()
    val json = serializer.toJson(value, value.javaClass)
    store(key, json)
  }

  override fun retrieveLong(key: String): Long? {
    val result = preferences.getLong(key, -1L)
    return if (result != -1L) result else null
  }

  override fun retrieveString(key: String): String? {
    return preferences.getString(key, null)
  }

  override fun retrieveInteger(key: String): Int? {
    val result = preferences.getInt(key, -1)
    return if (result != -1) result else null
  }

  override fun retrieveBoolean(key: String): Boolean {
    return preferences.getBoolean(key, false)
  }

  override fun <T : Any> retrieve(key: String, clazz: KClass<T>): T? {
    val serializer = KotlinJsonSerializer.getInstance()
    val json = retrieveString(key) ?: return null

    @Suppress("UNCHECKED_CAST")
    return serializer.fromJson(json, clazz) as T
  }

  override fun remove(key: String) {
    preferences.edit { remove(key) }
  }

  override fun clear() {
    preferences.edit { clear() }
  }

  companion object {
    private const val ENCRYPTED_PREFERENCES_KEY_ALIAS = "ENCRYPTED_PREFERENCES_KEY_ALIAS"
    private const val ENCRYPTED_PREFERENCES_FILE_NAME = "encrypted_preferences_file"
  }
}

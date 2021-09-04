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

package com.ericafenyo.bikediary.util.credentials

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.shared.json.JsonSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EncryptedCredentialsManager @Inject constructor(
  @ApplicationContext context: Context,
  private val jsonSerialize: JsonSerializer

) : CredentialsManager {
  private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

  private val prefs: Lazy<SharedPreferences> = lazy {
    EncryptedSharedPreferences.create(
      PREF_FILE_NAME,
      masterKey,
      context.applicationContext,
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
  }

  override suspend fun saveCredentials(credentials: Credentials) {
    val json = jsonSerialize.encode(Credentials.serializer(), credentials)
    prefs.value.edit { putString(PREF__KEY_CREDENTIALS, json) }
  }

  override suspend fun getCredentials(): Credentials {
    val defaultValue by lazy { jsonSerialize.encode(Credentials.serializer(), Credentials()) }
    val json = prefs.value.getString(PREF__KEY_CREDENTIALS, null) ?: defaultValue
    return jsonSerialize.decode(Credentials.serializer(), json)
  }

  override suspend fun clearCredentials() {
    prefs.value.edit {
      remove(PREF__KEY_CREDENTIALS)
    }
  }

  override suspend fun hasValidCredentials(): Boolean {
    return getCredentials().isValid()
  }

  companion object {
    private const val PREF_FILE_NAME = "encrypted_credentials_shared_prefs"
    private const val PREF__KEY_CREDENTIALS = "com.ericafenyo.bikediary.CREDENTIALS_PREFS_KEY"
  }
}

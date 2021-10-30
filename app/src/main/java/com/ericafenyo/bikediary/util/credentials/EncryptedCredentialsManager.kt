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

import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.model.CredentialsManager
import com.ericafenyo.libs.storage.EncryptedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedCredentialsManager @Inject constructor(
  private val storage: EncryptedPreferences
) : CredentialsManager {

  override suspend fun saveCredentials(credentials: Credentials) {
    storage.store(PREF_KEY_AUTH_CREDENTIALS, credentials)
  }

  override suspend fun getCredentials(): Credentials {
    return storage.retrieve(PREF_KEY_AUTH_CREDENTIALS, Credentials::class) ?: Credentials()
  }

  override suspend fun clearCredentials() {
    storage.remove(PREF_KEY_AUTH_CREDENTIALS)
  }

  override suspend fun hasValidCredentials(): Boolean {
    return getCredentials().isValid()
  }

  companion object {
    private const val PREF_KEY_AUTH_CREDENTIALS = "com.ericafenyo.bikediary.PREF_AUTH_CREDENTIALS"
  }
}

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

package com.ericafenyo.bikediary.di

import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.ericafenyo.bikediary.app.LocationProvider
import com.ericafenyo.bikediary.app.internal.LocationProviderImpl
import com.ericafenyo.bikediary.data.settings.PreferenceStorage
import com.ericafenyo.bikediary.data.settings.SharedPreferenceStorage
import com.ericafenyo.bikediary.model.CredentialsManager
import com.ericafenyo.bikediary.util.credentials.EncryptedCredentialsManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

  @Binds
  @Singleton
  abstract fun bindCredentialsManager(impl: EncryptedCredentialsManager): CredentialsManager

  @Module
  @InstallIn(SingletonComponent::class)
  object Providers {
    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage {
      return SharedPreferenceStorage(context)
    }

    @Provides
    fun provideWifiManager(@ApplicationContext context: Context): WifiManager =
      context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
      context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
          as ConnectivityManager

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
      context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
          as ClipboardManager

    @Provides
    @Singleton
    fun provideLocationProvider(instance: LocationProviderImpl): LocationProvider = instance
  }
}

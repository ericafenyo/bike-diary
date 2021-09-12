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

package com.ericafenyo.bikediary.network

import com.apollographql.apollo.ApolloClient
import com.ericafenyo.bikediary.network.adventure.AdventureService
import com.ericafenyo.bikediary.network.adventure.AdventureServiceImpl
import com.ericafenyo.bikediary.network.user.UserService
import com.ericafenyo.bikediary.network.user.UserServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

  @Binds
  @Singleton
  abstract fun bindUserService(impl: UserServiceImpl): UserService

  @Binds
  @Singleton
  abstract fun bindAdventureService(impl: AdventureServiceImpl): AdventureService

  @Module
  @InstallIn(SingletonComponent::class)
  object Providers {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
      if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY).apply {
          addInterceptor(this)
        }
      }
    }.build()

    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient = ApolloClient.builder()
      .serverUrl("${BuildConfig.API_SERVER_URL}/graphql")
      .okHttpClient(okHttpClient)
      .build()

//    @Provides
//    @Singleton
//    fun provideAdventureRemoteDataSource(
//      client: ApolloClient
//    ): AdventureService = AdventureServiceImpl(client)
  }
}

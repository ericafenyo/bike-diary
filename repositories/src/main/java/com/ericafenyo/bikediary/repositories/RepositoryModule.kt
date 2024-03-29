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

package com.ericafenyo.bikediary.repositories

import com.ericafenyo.bikediary.network.account.AccountService
import com.ericafenyo.bikediary.network.adventure.AdventureService
import com.ericafenyo.bikediary.repositories.account.AccountRepository
import com.ericafenyo.bikediary.repositories.account.accountRepository
import com.ericafenyo.bikediary.repositories.adventure.AdventureLocalDataSource
import com.ericafenyo.bikediary.repositories.adventure.AdventureRepository
import com.ericafenyo.bikediary.repositories.adventure.internal.adventureRepository
import com.ericafenyo.bikediary.repositories.photo.PhotoRepository
import com.ericafenyo.bikediary.repositories.photo.internal.PhotoRepositoryImpl
import com.ericafenyo.bikediary.repositories.settings.SettingsRepository
import com.ericafenyo.bikediary.repositories.settings.SettingsRepositoryImpl
import com.ericafenyo.bikediary.repositories.user.UserRepository
import com.ericafenyo.bikediary.repositories.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

  @Binds
  @Singleton
  abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

  @Binds
  @Singleton
  abstract fun bindPhotoRepository(repository: PhotoRepositoryImpl): PhotoRepository

  companion object {

    @Singleton
    @Provides
    fun provideAccountRepository(service: AccountService): AccountRepository {
      return accountRepository(service)
    }

    @Singleton
    @Provides
    fun provideAdventureRepository(
      service: AdventureService,
      localSource: AdventureLocalDataSource
    ): AdventureRepository {
      return adventureRepository(service, localSource)
    }
  }
}

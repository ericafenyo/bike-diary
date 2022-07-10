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

package com.ericafenyo.bikediary.repositories.adventure.internal

import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.bikediary.network.adventure.AdventureService
import com.ericafenyo.bikediary.repositories.adventure.AdventureLocalDataSource
import com.ericafenyo.bikediary.repositories.adventure.AdventureRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@Singleton
class AdventureRepositoryImpl @Inject constructor(
  private val service: AdventureService,
  private val localSource: AdventureLocalDataSource,
) : AdventureRepository {

  override fun adventures(): Flow<List<Adventure>> {
    return flow { emit(service.getAdventures()) }
//    return localSource.adventures()
  }

  override fun adventure(id: String): Flow<Adventure> = localSource.adventure(id)

  override suspend fun updateAdventures(refresh: Boolean): Boolean {
    if (!refresh) {
      return true
    }

    return runCatching { service.getAdventures() }
      .onSuccess { adventures -> localSource.bulkInsert(adventures) }.isSuccess
  }

  override suspend fun synchronizeAdventures() {
    val unprocessedAdventures = localSource.getUnprocessedAdventures()
    if (unprocessedAdventures.isNotEmpty()) {
      val syncedAdventures = service.synchronizeAdventures(unprocessedAdventures)
      if (syncedAdventures.isNotEmpty()) {
        localSource.bulkInsert(syncedAdventures)
      }
    } else {
      Timber.d("No adventures to sync")
    }
  }
}

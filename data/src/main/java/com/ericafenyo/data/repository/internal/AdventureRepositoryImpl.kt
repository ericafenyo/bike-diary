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

package com.ericafenyo.data.repository.internal

import com.ericafenyo.data.database.AdventureEntity
import com.ericafenyo.data.repository.AdventureRepository
import com.ericafenyo.tracker.data.Adventure
import com.ericafenyo.tracker.data.api.BikeDiaryService
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Singleton
@OptIn(ExperimentalCoroutinesApi::class)
class AdventureRepositoryImpl @Inject constructor(
  private val service: BikeDiaryService,
  private val mapper: AdventureMapper,
  private val localStore: AdventureLocalDataSource,
) : AdventureRepository {

  override suspend fun adventures(): Flow<List<Adventure>> = flow {
    val adventures = localStore.getAdventures();
    emit(toEntities(adventures))
  }

  override suspend fun adventure(): Flow<Adventure> = localStore.adventure().map { mapper.map(it) }

  private suspend fun toEntities(adventures: List<AdventureEntity>): List<Adventure> {
    return adventures.map { adventure -> mapper.map(adventure) }
  }
}

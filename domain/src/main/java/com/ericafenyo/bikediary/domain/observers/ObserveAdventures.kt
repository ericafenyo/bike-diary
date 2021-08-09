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

package com.ericafenyo.bikediary.domain.observers

import com.ericafenyo.bikediary.di.qualifier.IODispatcher
import com.ericafenyo.bikediary.domain.FlowInteractor
import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.bikediary.repositories.adventure.AdventureRepository
import com.ericafenyo.tracker.data.model.Result
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


class ObserveAdventures @Inject constructor(
  private val repository: AdventureRepository,
  @IODispatcher dispatcher: CoroutineDispatcher,
) : FlowInteractor<List<Adventure>>(dispatcher) {
  override fun execute(): Flow<Result<List<Adventure>>> = flow {
    try {
      repository.adventures().collect { adventures ->
        emit(Result.Success(adventures))
      }
    } catch (exception: Exception) {
      emit(Result.Error(exception))
    }
  }
}

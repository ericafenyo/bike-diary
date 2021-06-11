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

package com.ericafenyo.tracker.domain.bmi

import com.ericafenyo.bikediary.data.weight.WeightRepository
import com.ericafenyo.tracker.data.api.repository.UserRepository
import com.ericafenyo.tracker.di.qualifier.IODispatcher
import com.ericafenyo.tracker.domain.ParameterizedInteractor
import com.ericafenyo.tracker.domain.auth.AuthenticatedUserInfo
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

typealias BodyMassIndexParams = CalibrateBodyMassIndexInteractor.Params

class CalibrateBodyMassIndexInteractor @Inject constructor(
  private val authenticatedUserInfo: AuthenticatedUserInfo,
  private val weights: WeightRepository,
  private val users: UserRepository,
  @IODispatcher dispatcher: CoroutineDispatcher,
) : ParameterizedInteractor<BodyMassIndexParams, Unit>(dispatcher) {

  override suspend fun execute(params: BodyMassIndexParams) {
    if (authenticatedUserInfo.isAuthenticated()) {
      val user = authenticatedUserInfo.getData()
      user?.let { users.update(user.copy(height = params.height, weight = params.weight)) }
    } else {

    }
  }

  data class Params(
    val weight: Double,
    val height: Double,
  )
}
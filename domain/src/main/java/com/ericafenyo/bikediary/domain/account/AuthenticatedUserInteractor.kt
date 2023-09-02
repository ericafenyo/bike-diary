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

package com.ericafenyo.bikediary.domain.account

import com.ericafenyo.bikediary.domain.FlowInteractor
import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.repositories.authentication.AuthenticatedUser
import com.ericafenyo.bikediary.repositories.authentication.DefaultAuthenticatedUser
import com.ericafenyo.bikediary.repositories.user.UserRepository
import com.ericafenyo.tracker.util.CoroutineDispatchers
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AuthenticatedUserInteractor @Inject constructor(
  private val repository: UserRepository,
  private val credentials: Credentials,
  dispatchers: CoroutineDispatchers
) : FlowInteractor<AuthenticatedUser>(dispatchers.io) {
  override fun execute(): Flow<AuthenticatedUser> {
    return repository.user().mapLatest { user -> DefaultAuthenticatedUser(user, credentials) }
  }
}

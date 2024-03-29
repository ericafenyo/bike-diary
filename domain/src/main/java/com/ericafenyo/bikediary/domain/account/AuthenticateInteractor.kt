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

import com.ericafenyo.bikediary.domain.ParameterizedInteractor
import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.repositories.account.AccountRepository
import com.ericafenyo.bikediary.repositories.user.UserRepository
import com.ericafenyo.tracker.util.CoroutineDispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticateInteractor @Inject constructor(
  private val repository: AccountRepository,
  dispatchers: CoroutineDispatchers
) : ParameterizedInteractor<Pair<String, String>, Credentials>(dispatchers.io) {
  override suspend fun execute(params: Pair<String, String>): Credentials {
    val (email, password) = params
    return repository.authenticate(email, password)
  }
}

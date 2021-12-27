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

package com.ericafenyo.bikediary.ui.authentication.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericafenyo.bikediary.flux.ActionDispatcher
import com.ericafenyo.bikediary.ui.authentication.verify.VerifyAccountViewModel.VerifyAccountAction
import com.ericafenyo.bikediary.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyAccountViewModel @Inject constructor() : ViewModel(),
  ActionDispatcher<VerifyAccountAction> {

  private val _events = MutableLiveData<Event<VerifyAccountAction>>()
  val events: LiveData<Event<VerifyAccountAction>> get() = _events

  enum class VerifyAccountAction { RESEND_CODE, VERIFY_CODE }

  override fun dispatch(action: VerifyAccountAction) {
    _events.value = Event(action)
  }

  fun verifyCode(code: Int) {
    TODO()
  }

  fun resendCode() {
    TODO()
  }
}

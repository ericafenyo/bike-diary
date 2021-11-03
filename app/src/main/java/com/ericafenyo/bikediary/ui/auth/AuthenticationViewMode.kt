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

package com.ericafenyo.bikediary.ui.auth

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericafenyo.bikediary.domain.user.AuthenticateInteractor
import com.ericafenyo.bikediary.flux.Dispatcher
import com.ericafenyo.bikediary.model.UIState
import com.ericafenyo.bikediary.ui.auth.AuthenticationViewMode.AuthenticationAction
import com.ericafenyo.bikediary.util.Event
import com.ericafenyo.bikediary.util.NetworkUtils
import com.ericafenyo.bikediary.widget.dialog.Alert
import com.ericafenyo.bikediary.widget.dialog.Alert.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class AuthenticationViewMode @Inject constructor(
  private val networkUtils: NetworkUtils,
) : ViewModel(), Dispatcher<AuthenticationAction> {
  // States
  private val _state = MutableLiveData<UIState>()
  val state: LiveData<UIState> get() = _state

  private val _message = MutableLiveData<Message>()
  val message: LiveData<Message> get() = _message

  private val _events = MutableLiveData<Event<AuthenticationAction>>()
  val events: LiveData<Event<AuthenticationAction>> get() = _events

  enum class AuthenticationAction { VERIFY_CODE, RESEND_CODE }

  fun verifyCode(code: Int) {
    Timber.d("Validating code")
  }

  override fun dispatch(action: AuthenticationAction) {
    _events.value = Event(action)
  }

  fun resendCode() {
    Timber.d("Resending code")
  }
}

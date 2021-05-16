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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ericafenyo.bikediary.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
  private val _loginAction = MutableLiveData<Event<Unit>>()
  val loginAction: LiveData<Event<Unit>> get() = _loginAction

  private val _launchRegisterAction = MutableLiveData<Event<Unit>>()
  val launchRegisterAction: LiveData<Event<Unit>> get() = _launchRegisterAction

  private val _launchForgotPasswordAction = MutableLiveData<Event<Unit>>()
  val launchForgotPasswordAction: LiveData<Event<Unit>> get() = _launchForgotPasswordAction


  // Android layout xml action = android:onClick="@{() -> model.onLogin()}"
  fun onLogin() {
    _loginAction.value = Event(Unit)
  }

  // Android layout xml action = android:onClick="@{() -> model.onRegister()}"
  fun onRegister() {
    _launchRegisterAction.value = Event(Unit)
  }

  // Android layout xml action = android:onClick="@{() -> model.onForgetPassword()}"
  fun onForgetPassword() {
    _launchForgotPasswordAction.value = Event(Unit)
  }
}

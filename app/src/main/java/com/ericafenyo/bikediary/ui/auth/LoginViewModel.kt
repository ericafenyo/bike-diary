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
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.domain.user.AuthenticateInteractor
import com.ericafenyo.bikediary.model.Credentials
import com.ericafenyo.bikediary.model.HttpException
import com.ericafenyo.bikediary.model.UIState
import com.ericafenyo.bikediary.model.isNotFound
import com.ericafenyo.bikediary.model.isUnauthorized
import com.ericafenyo.bikediary.util.Event
import com.ericafenyo.bikediary.util.NetworkUtils
import com.ericafenyo.bikediary.widget.dialog.Alert
import com.ericafenyo.tracker.data.model.Result
import com.ericafenyo.tracker.data.model.getOrElse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val networkUtils: NetworkUtils,
  private val authenticateInteractor: AuthenticateInteractor,
  private val credentialsManager: com.ericafenyo.bikediary.model.CredentialsManager
) : ViewModel() {
  // Messages
  private val _message = MutableLiveData<Alert.Message>()
  val message: LiveData<Alert.Message> get() = _message

  // States
  private val _state = MutableLiveData<UIState>()
  val state: LiveData<UIState> get() = _state

  // UI Events
  private val _launchLoginAction = MutableLiveData<Event<Unit>>()
  val launchLoginAction: LiveData<Event<Unit>> get() = _launchLoginAction

  private val _launchRegisterAction = MutableLiveData<Event<Unit>>()
  val launchRegisterAction: LiveData<Event<Unit>> get() = _launchRegisterAction

  private val _launchForgotPasswordAction = MutableLiveData<Event<Unit>>()
  val launchForgotPasswordAction: LiveData<Event<Unit>> get() = _launchForgotPasswordAction


  // Android layout xml action = android:onClick="@{() -> model.onLogin()}"
  fun onLogin() {
    _launchLoginAction.value = Event(Unit)
  }

  // Android layout xml action = android:onClick="@{() -> model.onRegister()}"
  fun onRegister() {
    _launchRegisterAction.value = Event(Unit)
  }

  // Android layout xml action = android:onClick="@{() -> model.onForgetPassword()}"
  fun onForgetPassword() {
    _launchForgotPasswordAction.value = Event(Unit)
  }

  fun authenticate(email: String, password: String) {
    viewModelScope.launch {
      // Check for internet connectivity
      if (!networkUtils.hasNetworkConnection()) {
        _message.value = Alert.Message(
          titleId = R.string.msg_no_connection_title,
          messageId = R.string.msg_no_connection_message
        )
        return@launch
      }

      // Start with a loading state
      _state.value = UIState(loading = true)
      authenticateInteractor.invoke(email to password).also { result ->
        when (result) {
          is Result.Success -> {
            credentialsManager.saveCredentials(result.getOrElse { Credentials() })
            _state.value = UIState(success = true)
          }
          is Result.Error -> {
            _state.value = UIState(error = true)
            handleLoginErrors(result.exception)
          }
        }
      }
    }
  }

  private fun handleLoginErrors(exception: Exception) {
    if (exception is HttpException) {
      when {
        exception.isUnauthorized() || exception.isNotFound() -> {
          _message.value = Alert.Message(
            titleId = R.string.label_oops,
            messageId = R.string.error_wrong_login_credentials
          )
        }
      }
    } else {
      _message.value = Alert.Message(
        titleId = R.string.msg_no_connection_title,
        messageId = R.string.msg_no_connection_message
      )
    }
  }
}

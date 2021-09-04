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

package com.ericafenyo.bikediary.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.domain.user.AccountParams
import com.ericafenyo.bikediary.domain.user.AddUserInteractor
import com.ericafenyo.bikediary.model.UIState
import com.ericafenyo.bikediary.shared.SimpleResult
import com.ericafenyo.bikediary.ui.register.RegisterViewModel.Action.CREATE_ACCOUNT
import com.ericafenyo.bikediary.util.Event
import com.ericafenyo.bikediary.util.NetworkUtils
import com.ericafenyo.bikediary.widget.dialog.Alert
import com.ericafenyo.tracker.data.model.simplify
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(
  private val networkUtils: NetworkUtils,
  private val addUserInteractor: AddUserInteractor
) : ViewModel() {
  enum class Action { CREATE_ACCOUNT, LAUNCH_LOGIN, LAUNCH_REGISTER }

  // Messages
  private val _message = MutableLiveData<Alert.Message>()
  val message: LiveData<Alert.Message> get() = _message

  // Events
  private val _events = MutableLiveData<Event<Action>>()
  val events: LiveData<Event<Action>> get() = _events

  // States
  private val _state = MutableLiveData<UIState>()
  val state: LiveData<UIState> get() = _state

  // Android layout xml action = android:onClick="@{() -> model.onRegister()}"
  fun register() {
    _events.value = Event(CREATE_ACCOUNT)
  }

  fun createAccount(
    firstName: String,
    lastName: String,
    email: String,
    password: String
  ) {
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
      addUserInteractor(AccountParams(firstName, lastName, email, password)).also { result ->
        when (result.simplify()) {
          is SimpleResult.Success -> {
            _state.value = UIState(success = true)
          }
          is SimpleResult.Error -> {
            _state.value = UIState(error = true)
          }
        }
      }
    }
  }
}

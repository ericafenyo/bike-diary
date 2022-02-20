/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Eric Afenyo
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

package com.ericafenyo.bikediary.ui.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.ui.components.buttons.LoadingButton
import com.ericafenyo.bikediary.ui.components.inputs.PasswordTextField
import com.ericafenyo.bikediary.util.Validator

@Composable
fun Login(
  onAction: (LoginAction) -> Unit,
  onNavigateUp: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colors.background)
  ) {
    SmallTopAppBar(title = {
      Text(text = stringResource(id = string.title_login))
    }, navigationIcon = {
      IconButton(onClick = onNavigateUp) {
        Icon(Icons.Filled.ArrowBack, null)
      }
    })

    Spacer(modifier = Modifier.height(16.dp))

    var email by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
      value = email,
      onValueChange = { email = it },
      label = { Text(text = stringResource(id = string.input_hint_email)) },
      singleLine = true,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
      )
    )

    // Password field
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordFocusRequester = remember { FocusRequester() }
    PasswordTextField(
      label = stringResource(id = string.input_hint_password),
      value = password,
      onChange = { value ->
        password = value
        isPasswordError = !isPasswordError
      },
      keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
      error = if (isPasswordError) {
        passwordErrorMessage
      } else {
        null
      },

      info = "This is an info message",
      modifier = Modifier.focusRequester(passwordFocusRequester),
    )

    Spacer(modifier = Modifier.height(32.dp))

    LoadingButton(
      text = stringResource(id = string.action_login), onClick = {
        val error = validateEmails(email)
        isPasswordError = error != null
        if (isPasswordError) {
          passwordErrorMessage
        }
//        onAction.invoke(LoginAction.Authenticate(email.value, password))
      }, modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
    )
  }

  @Composable
  fun validateEmails(email: String): String? {
    // Validate email
    if (email.isBlank()) {
      return stringResource(id = string.input_error_field_required)
    }

    if (!Validator.EMAIL_REGEX.matches(email)) {
      return stringResource(id = string.input_error_invalid_email)
    }

    return null
  }
}


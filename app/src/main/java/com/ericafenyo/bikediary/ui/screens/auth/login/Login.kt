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

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.model.UIState
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.ui.components.buttons.Button
import com.ericafenyo.bikediary.ui.components.dialog.Alert
import com.ericafenyo.bikediary.ui.components.dialog.AlertMessage
import com.ericafenyo.bikediary.ui.components.dialog.ColorTheme.CRITICAL
import com.ericafenyo.bikediary.ui.components.dialog.ColorTheme.SUCCESS
import com.ericafenyo.bikediary.ui.components.inputs.PasswordTextField
import com.ericafenyo.bikediary.ui.components.inputs.TextField
import com.ericafenyo.bikediary.ui.screens.auth.login.ValidationRule.INVALID_EMAIL
import com.ericafenyo.bikediary.ui.screens.auth.login.ValidationRule.REQUIRE_EMAIL
import com.ericafenyo.bikediary.ui.screens.auth.login.ValidationRule.REQUIRE_PASSWORD
import com.ericafenyo.bikediary.ui.screens.auth.login.ValidationRule.UNSPECIFIED
import com.ericafenyo.bikediary.util.Validator

enum class ValidationRule {
  REQUIRE_EMAIL,
  REQUIRE_PASSWORD,
  INVALID_EMAIL,
  UNSPECIFIED,
}

@Composable
fun Login(
  onAction: (LoginAction) -> Unit,
  onNavigateUp: () -> Unit,
  state: State<UIState> = mutableStateOf(UIState()),
  message: State<AlertMessage?> = mutableStateOf(null)
) {
  var password by remember { mutableStateOf("") }
  val passwordFocusRequester = remember { FocusRequester() }
  var email by remember { mutableStateOf("") }
  var isAlertVisible by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  var rule by remember { mutableStateOf(UNSPECIFIED) }

  fun hasValidInputs(email: String, password: String): Boolean {
    // Order is important
    return when {
      email.isBlank() -> {
        rule = REQUIRE_EMAIL; false
      }
      !Validator.EMAIL_REGEX.matches(email) -> {
        rule = INVALID_EMAIL; false
      }
      password.isBlank() -> {
        rule = REQUIRE_PASSWORD; false
      }
      else -> {
        rule = UNSPECIFIED; true
      }
    }
  }

  fun invalidateRules() {
    rule = UNSPECIFIED
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colors.background)
  ) {
    TopAppBar(
      backgroundColor = MaterialTheme.colors.surface,
      title = { Text(text = stringResource(id = string.title_login)) },
      navigationIcon = {
        IconButton(onClick = onNavigateUp) { Icon(Icons.Filled.ArrowBack, null) }
      })

    Spacer(modifier = Modifier.height(16.dp))

    message.value?.let { value ->
      Alert(
        title = stringResource(id = value.titleId),
        message = stringResource(id = value.messageId),
        appearance = if (state.value.success) SUCCESS else CRITICAL,
        modifier = Modifier.padding(horizontal = 24.dp),
        onDismissed = { isAlertVisible = false }
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
      label = stringResource(id = string.input_hint_email),
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
      value = email,
      enabled = !state.value.loading,
      onChange = { value ->
        email = value
        if (rule != UNSPECIFIED) {
          invalidateRules()
        }
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
      ),
      error = when (rule) {
        REQUIRE_EMAIL -> stringResource(id = string.input_error_field_required)
        INVALID_EMAIL -> stringResource(id = string.input_error_invalid_email)
        else -> null
      }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PasswordTextField(
      label = stringResource(id = string.input_hint_password),
      value = password,
      enabled = !state.value.loading,
      onChange = { value ->
        password = value
        if (rule != UNSPECIFIED) {
          invalidateRules()
        }
      },
      keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
      error = when (rule) {
        REQUIRE_PASSWORD -> stringResource(id = string.input_error_field_required)
        else -> null
      },

      modifier = Modifier
        .padding(horizontal = 24.dp)
        .focusRequester(passwordFocusRequester)
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
      text = stringResource(id = string.action_login),
      isLoading = state.value.loading,
      enabled = !state.value.loading,
      onClick = {
        if (hasValidInputs(email, password)) {
          onAction.invoke(LoginAction.Authenticate(email, password))
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
    )
  }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
  AppTheme {
    Login(
      onAction = {},
      onNavigateUp = {}
    )
  }
}

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

package com.ericafenyo.bikediary.ui.components.inputs

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.components.inputs.field.FieldMessage

@Composable
fun PasswordTextField(
  modifier: Modifier = Modifier,
  value: String,
  label: String = "Password",
  enabled: Boolean = true,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  error: String? = null,
  info: String? = null,
  onChange: (String) -> Unit,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
  val showPassword = remember { mutableStateOf(false) }
  val isFocused = interactionSource.collectIsFocusedAsState().value

  OutlinedTextField(
    modifier = Modifier
      .fillMaxWidth()
      .then(modifier),
    value = value,
    onValueChange = onChange,
    label = { Text(text = label) },
    singleLine = true,
    enabled = enabled,
    interactionSource = interactionSource,
    visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
    keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
    keyboardActions = keyboardActions,
    trailingIcon = {
      IconButton(onClick = { showPassword.value = !showPassword.value }) {
        Icon(
          if (showPassword.value) Icons.Eye else Icons.EyeOff,
          contentDescription = "Visibility",
        )
      }
    },
  )

  FieldMessage(
    modifier = Modifier.padding(start = 24.dp),
    error = error?.let {
      {
        Text(
          text = error,
          style = MaterialTheme.typography.body1,
          color = MaterialTheme.colors.error
        )
      }
    },
    info = info?.let {
      if (isFocused) {
        {
          Text(
            text = info,
            style = MaterialTheme.typography.body1,
            color = Color.Blue
          )
        }
      } else null
    }
  )
}


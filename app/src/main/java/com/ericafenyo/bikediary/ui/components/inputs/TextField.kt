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

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.components.inputs.field.FieldMessage

@Composable
fun TextField(
  modifier: Modifier = Modifier,
  value: String,
  label: String? = null,
  singleLine: Boolean = true,
  enabled: Boolean = true,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  placeholder: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
  error: String? = null,
  info: String? = null,
  onChange: (String) -> Unit,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
  val isFocused = interactionSource.collectIsFocusedAsState().value

  OutlinedTextField(
    modifier = Modifier.fillMaxWidth().then(modifier),
    value = value,
    onValueChange = onChange,
    label = label?.let { { Text(text = label) } },
    singleLine = singleLine,
    enabled = enabled,
    placeholder = placeholder,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    interactionSource = interactionSource,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
  )

  FieldMessage(
    modifier = Modifier.padding(start = 24.dp),
    error = error?.let {
      {
        Text(
          text = error,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.error
        )
      }
    },
    info = info?.let {
      if (isFocused) {
        {
          Text(
            text = info,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Blue
          )
        }
      } else null
    }
  )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TextFieldPreview() {
  AppTheme {
    TextField(
      label = "Email",
      onChange = {},
      value = ""
    )
  }
}



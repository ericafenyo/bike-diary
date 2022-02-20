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

package com.ericafenyo.bikediary.ui.components.dialog

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.LocalColors
import com.ericafenyo.bikediary.theme.toWarning
import com.ericafenyo.bikediary.ui.components.dialog.ColorTheme.CRITICAL
import com.ericafenyo.bikediary.ui.components.dialog.ColorTheme.INFO
import com.ericafenyo.bikediary.ui.components.dialog.ColorTheme.SUCCESS
import com.ericafenyo.bikediary.ui.components.dialog.ColorTheme.WARNING

@Composable
fun Alert(
  title: String,
  message: String,
  modifier: Modifier = Modifier,
  actions: (@Composable () -> Unit)? = null,
  onDismissed: (() -> Unit)? = null,
  theme: ColorTheme = ColorTheme.INFO
) {
  val themeColor = when (theme) {
    INFO -> MaterialTheme.colors.toWarning()
    SUCCESS -> MaterialTheme.colors.toWarning()
    WARNING -> MaterialTheme.colors.toWarning()
    CRITICAL -> MaterialTheme.colors.toWarning()
  }

  CompositionLocalProvider(
    LocalColors provides themeColor
  ) {
    val mainColor = LocalColors.current.primary
    val backgroundColor = LocalColors.current.background
    val borderColor = LocalColors.current.primary.copy(0.08f)
    val shape = MaterialTheme.shapes.small

    Box(
      modifier
        .fillMaxWidth()
        .background(backgroundColor)
        .clip(shape)
        .border(1.dp, borderColor, shape)
    ) {

    }
  }
}


/**
 * A data class containing properties for creating an [Alert]
 *
 * @property messageId String resource id of the message to show
 * @property titleId Optional string resource id of the title to show
 * @property actionId Optional string resource id of the button label
 * @property closable Optional make the dialog non dismissible set to false
 */
data class AlertMessage(
  @StringRes val titleId: Int,
  @StringRes val messageId: Int,
  @StringRes val actionId: Int = android.R.string.ok,
  val action: Int? = null,
  val closable: Boolean = true,
)

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AlertPreview() {
  AppTheme {
    Alert(
      title = "Invalid credentials",
      message = "Your email or password is invalid"
    )
  }
}


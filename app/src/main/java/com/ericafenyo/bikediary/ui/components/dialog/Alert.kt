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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.theme.LocalColors
import com.ericafenyo.bikediary.ui.theme.toCritical
import com.ericafenyo.bikediary.ui.theme.toInfo
import com.ericafenyo.bikediary.ui.theme.toSuccess
import com.ericafenyo.bikediary.ui.theme.toWarning
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
  appearance: ColorTheme = INFO,
  isInline: Boolean = false,
) {
  val (themeColor: ColorScheme, icon: Painter) = when (appearance) {
    INFO -> MaterialTheme.colorScheme.toInfo() to Icons.InformationCircle
    SUCCESS -> MaterialTheme.colorScheme.toSuccess() to Icons.CheckCircle
    WARNING -> MaterialTheme.colorScheme.toWarning() to Icons.Exclamation
    CRITICAL -> MaterialTheme.colorScheme.toCritical() to Icons.ExclamationCircle
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
        .clip(shape)
        .background(backgroundColor)
        .border(1.5.dp, borderColor, shape)
        .padding(16.dp)
    ) {
      ConstraintLayout(
        Modifier.fillMaxWidth()
      ) {
        val (iconRef, dismissRef, titleRef, messageRef, actionRef) = createRefs()

        Icon(painter = icon,
          contentDescription = null,
          tint = mainColor,
          modifier = Modifier
            .padding(end = 8.dp)
            .size(16.dp)
            .constrainAs(iconRef) {
              start.linkTo(parent.start)
              top.linkTo(parent.top)
            })

        Text(
          text = title, modifier = Modifier.constrainAs(titleRef) {
            top.linkTo(iconRef.top)
            bottom.linkTo(iconRef.bottom)
            start.linkTo(iconRef.end)
          }, style = MaterialTheme.typography.titleMedium
        )

        if (onDismissed != null) {
          IconButton(onClick = onDismissed,
            modifier = Modifier
              .offset(x = (16).dp, y = (-16).dp)
              .constrainAs(dismissRef) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
              }) {
            Icon(
              painter = Icons.Close,
              contentDescription = stringResource(string.label_close),
              modifier = Modifier.size(18.dp)
            )
          }
        }

        val barrier = createBottomBarrier(iconRef, titleRef)
        Text(
          text = message, modifier = Modifier
            .padding(top = 8.dp)
            .constrainAs(messageRef) {
              top.linkTo(barrier)
              start.linkTo(titleRef.start)
              end.linkTo(parent.end)
              width = Dimension.fillToConstraints
            }, style = MaterialTheme.typography.bodyMedium
        )
      }
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
    Alert(title = "Invalid credentials",
      message = "Your email or password is invalid",
      onDismissed = {})
  }
}


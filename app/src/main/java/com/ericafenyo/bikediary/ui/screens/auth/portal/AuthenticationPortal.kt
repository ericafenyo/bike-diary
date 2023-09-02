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

package com.ericafenyo.bikediary.ui.screens.auth.portal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.util.space

@Composable
fun AuthenticationPortal(
  onClickLogin: () -> Unit,
  onClickRegister: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 24.dp),
  ) {
    Text(
      text = stringResource(string.auth_portal_page_title),
      fontWeight = FontWeight.W400,
      fontSize = 24.sp,
      lineHeight = 32.sp,
      style = TextStyle(textAlign = TextAlign.Center),
      modifier = Modifier
        .fillMaxWidth()
        .space(top = 38.dp),
    )
    Text(
      text = stringResource(string.auth_portal_page_description),
      fontWeight = FontWeight.W400,
      fontSize = 14.sp,
      lineHeight = 20.sp,
      letterSpacing = 0.25.sp,
      style = TextStyle(textAlign = TextAlign.Center),
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.weight(1f))
    Button(
      onClick = onClickLogin,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .height(48.dp),
      elevation = null

    ) {
      Text(
        text = stringResource(id = string.action_login),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
      )
    }
    OutlinedButton(
      onClick = onClickRegister,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 32.dp)
        .height(48.dp),
    ) {
      Text(
        text = stringResource(id = string.action_sign_up),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
      )
    }
  }
}

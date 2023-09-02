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

package com.ericafenyo.bikediary.ui.screens.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.theme.titleMedium

@Composable
fun ThemeSwitcher(
  isChecked: Boolean,
  onChecked: (Boolean) -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    onClick = { onChecked.invoke(!isChecked) }
  ) {
    ConstraintLayout(modifier = Modifier.padding(horizontal = 16.dp)) {
      val (iconRef, labelRef, switchRef) = createRefs()

      Icon(painter = Icons.BrightnessMedium,
        contentDescription = null,
        modifier = Modifier.constrainAs(iconRef) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
          bottom.linkTo(parent.bottom)
        })

      Text(text = stringResource(id = string.profile_label_dark_mode),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.constrainAs(labelRef) {
          top.linkTo(parent.top)
          start.linkTo(iconRef.end, 16.dp)
          bottom.linkTo(parent.bottom)
        })

      Switch(checked = isChecked,
        onCheckedChange = onChecked, modifier = Modifier
          .offset(x = 8.dp)
          .constrainAs(switchRef) {
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
          })
    }
  }
}
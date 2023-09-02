/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2023 Eric Afenyo
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

package com.ericafenyo.bikediary.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.theme.extendedColors

@Composable
fun ToolBar(
  title: String,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
  scrollBehavior: TopAppBarScrollBehavior? = null
) {

  val toolBarColors = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.extendedColors.surfaceContainer
  )

  TopAppBar(
    modifier = modifier,
    title = { Text(text = title) },
    navigationIcon = navigationIcon,
    actions = actions,
    windowInsets = windowInsets,
    colors = toolBarColors,
    scrollBehavior = scrollBehavior,
  )
}

@Preview
@Composable
fun ToolBarDarkPreview() {
  AppTheme(useDarkTheme = true) {
    ToolBar(title = "Dashboard")
  }
}

@Preview
@Composable
fun ToolBarLightPreview() {
  AppTheme(useDarkTheme = false) {
    ToolBar(title = "Dashboard")
  }
}

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

package com.ericafenyo.bikediary.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val color_primary_light = Color(0xff0060aa)
val color_background_light = Color(0xfffdfcff)
val color_surface_light = Color(0xffffffff)

val color_primary_dark = Color(0xffa0c9ff)
val color_background_dark = Color(0xff1a1c18)
val color_surface_dark = Color(0xff1b1b1b)

// Warning color theme
val color_warning_primary_light = Color(0xffFFEDD5)
val color_warning_background_light = Color(0xffFFEDD5)

val color_warning_primary_dark = Color(0xffFFEDD5)
val color_warning_background_dark = Color(0xffFFEDD5)

fun Colors.toWarning() = if (isLight) {
  lightColors(
    primary = color_warning_primary_light,
    background = color_warning_background_light
  )
} else {
  darkColors(
    primary = color_warning_primary_dark,
    background = color_warning_background_dark
  )
}

val LocalColors: ProvidableCompositionLocal<Colors> = compositionLocalOf { lightColors() }

val LightColors = lightColors(
  primary = color_primary_light,
  surface = color_surface_light,
  background = color_background_light,
)

val DarkColors = darkColors(
  primary = color_primary_dark,
  surface = color_surface_dark,
  background = color_background_dark,
)


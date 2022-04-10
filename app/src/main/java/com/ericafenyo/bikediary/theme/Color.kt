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
val color_background_light = Color(0xffe3e5e8)
val color_surface_light = Color(0xffffffff)

val color_primary_dark = Color(0xffa0c9ff)
val color_background_dark = Color(0xff151E27)
val color_surface_dark = Color(0xff1D2733)

val color_grey_900 = Color(0xff111827)
val color_grey_500 = Color(0xff6b7280)
val color_grey_200 = Color(0xffe5e7eb)

// Success color theme
val color_success_primary_light = Color(0xff22C55E)
val color_success_background_light = Color(0xffDCFCE7)

val color_success_primary_dark = Color(0xffFFEDD5)
val color_success_background_dark = Color(0xffFFEDD5)

fun Colors.toSuccess() = if (isLight) {
  lightColors(
    primary = color_success_primary_light,
    background = color_success_background_light
  )
} else {
  darkColors(
    primary = color_success_primary_dark,
    background = color_success_background_dark
  )
}

// Info color theme
val color_info_primary_light = Color(0xff3B82F6)
val color_info_background_light = Color(0xffDBEAFE)

val color_info_primary_dark = Color(0xffFFEDD5)
val color_info_background_dark = Color(0xffFFEDD5)

fun Colors.toInfo() = if (isLight) {
  lightColors(
    primary = color_info_primary_light,
    background = color_info_background_light
  )
} else {
  darkColors(
    primary = color_info_primary_dark,
    background = color_info_background_dark
  )
}

// Warning color theme
val color_warning_primary_light = Color(0xffF97316)
val color_warning_background_light = Color(0xffFFEDD5)

val color_warning_primary_dark = Color(0xffF97316)
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

// Critical color theme
val color_critical_primary_light = Color(0xffEF4444)
val color_critical_background_light = Color(0xffFEE2E2)

val color_critical_primary_dark = Color(0xffFFEDD5)
val color_critical_background_dark = Color(0xffFFEDD5)

fun Colors.toCritical() = if (isLight) {
  lightColors(
    primary = color_critical_primary_light,
    background = color_critical_background_light
  )
} else {
  darkColors(
    primary = color_critical_primary_dark,
    background = color_critical_background_dark
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
  onSurface = color_grey_900,
  onBackground = color_grey_900
)

val Colors.contentHigh
  get() = if (isLight) color_grey_900 else Color.White.copy(Alpha.high)

val Colors.contentMedium
  get() = if (isLight) color_grey_500 else Color.White.copy(Alpha.medium)

val Colors.contentDisabled
  get() = if (isLight) color_grey_200 else Color.White.copy(Alpha.disabled)

object Alpha {
  const val high: Float = 0.87f
  const val medium: Float = 0.60f
  const val disabled: Float = 0.38f
}
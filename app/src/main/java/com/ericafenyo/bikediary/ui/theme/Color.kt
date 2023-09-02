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

package com.ericafenyo.bikediary.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.ericafenyo.bikediary.model.Difficulty
import com.ericafenyo.bikediary.model.Difficulty.CHALLENGING
import com.ericafenyo.bikediary.model.Difficulty.DIFFICULT
import com.ericafenyo.bikediary.model.Difficulty.EASY
import com.ericafenyo.bikediary.model.Difficulty.EXTREME
import com.ericafenyo.bikediary.model.Difficulty.MODERATE

// Color for light theme
val color_primary_light = Color(0xFF436915)
val color_on_primary_light = Color(0xFFFFFFFF)
val color_primary_container_light = Color(0xFFC2F18D)
val color_on_primary_container_light = Color(0xFF0F2000)
val color_secondary_light = Color(0xFF57624A)
val color_on_secondary_light = Color(0xFFFFFFFF)
val color_secondary_container_light = Color(0xFFDBE7C8)
val color_on_secondary_container_light = Color(0xFF151E0B)
val color_tertiary_light = Color(0xFF386663)
val color_on_tertiary_light = Color(0xFFFFFFFF)
val color_tertiary_container_light = Color(0xFFBBECE8)
val color_on_tertiary_container_light = Color(0xFF00201F)
val color_background_light = Color(0xFFFDFCF5)
val color_on_background_light = Color(0xFF1B1C18)
val color_surface_light = Color(0xFFFDFCF5)
val color_on_surface_light = Color(0xFF1B1C18)
val color_level_easy_light = Color(0xFF15803d)
val color_level_moderate_light = Color(0xFF1d4ed8)
val color_level_challenging_light = Color(0xFF6d28d9)
val color_level_difficult_light = Color(0xFFa16207)
val color_level_extreme_light = Color(0xFFb91c1c)

// Color for dark theme
val color_primary_dark = Color(0xFFA7D474)
val color_on_primary_dark = Color(0xFF1E3700)
val color_primary_container_dark = Color(0xFF2D5000)
val color_on_primary_container_dark = Color(0xFFC2F18D)
val color_secondary_dark = Color(0xFFBFCBAD)
val color_on_secondary_dark = Color(0xFF2A331E)
val color_secondary_container_dark = Color(0xFF404A33)
val color_on_secondary_container_dark = Color(0xFFDBE7C8)
val color_tertiary_dark = Color(0xFFA0CFCC)
val color_on_tertiary_dark = Color(0xFF003735)
val color_tertiary_container_dark = Color(0xFF1F4E4B)
val color_on_tertiary_container_dark = Color(0xFFBBECE8)
val color_background_dark = Color(0xFF1B1C18)
val color_on_background_dark = Color(0xFFE3E3DB)
val color_surface_dark = Color(0xFF1B1C18)
val color_on_surface_dark = Color(0xFFE3E3DB)
val color_level_easy_dark = Color(0xFFA7D474)
val color_level_moderate_dark = Color(0xFFA7D474)
val color_level_challenging_dark = Color(0xFFA7D474)
val color_level_difficult_dark = Color(0xFFA7D474)
val color_level_extreme_dark = Color(0xFFA7D474)

val LightColors = lightColorScheme(
  primary = color_primary_light,
  onPrimary = color_on_primary_light,
  primaryContainer = color_primary_container_light,
  onPrimaryContainer = color_on_primary_container_light,
  secondary = color_secondary_light,
  onSecondary = color_on_secondary_light,
  secondaryContainer = color_secondary_container_light,
  onSecondaryContainer = color_on_secondary_container_light,
  tertiary = color_tertiary_light,
  onTertiary = color_on_tertiary_light,
  tertiaryContainer = color_tertiary_container_light,
  onTertiaryContainer = color_on_tertiary_container_light,
  surface = color_surface_light,
  onSurface = color_on_surface_light,
  background = color_background_light,
  onBackground = color_on_background_light
)

val DarkColors = darkColorScheme(
  primary = color_primary_dark,
  onPrimary = color_on_primary_dark,
  primaryContainer = color_primary_container_dark,
  onPrimaryContainer = color_on_primary_container_dark,
  secondary = color_secondary_dark,
  onSecondary = color_on_secondary_dark,
  secondaryContainer = color_secondary_container_dark,
  onSecondaryContainer = color_on_secondary_container_dark,
  tertiary = color_tertiary_dark,
  onTertiary = color_on_tertiary_dark,
  tertiaryContainer = color_tertiary_container_dark,
  onTertiaryContainer = color_on_tertiary_container_dark,
  surface = color_surface_dark,
  onSurface = color_on_surface_dark,
  background = color_background_dark,
  onBackground = color_on_background_dark
)

// Success color theme
val color_success_primary_light = Color(0xff22C55E)
val color_success_background_light = Color(0xffDCFCE7)
val color_success_primary_dark = Color(0xffFFEDD5)
val color_success_background_dark = Color(0xffFFEDD5)

// Info color theme
val color_info_primary_light = Color(0xff3B82F6)
val color_info_background_light = Color(0xffDBEAFE)
val color_info_primary_dark = Color(0xffFFEDD5)
val color_info_background_dark = Color(0xffFFEDD5)

// Warning color theme
val color_warning_primary_light = Color(0xffF97316)
val color_warning_background_light = Color(0xffFFEDD5)
val color_warning_primary_dark = Color(0xffF97316)
val color_warning_background_dark = Color(0xffFFEDD5)

// Critical color theme
val color_critical_primary_light = Color(0xffEF4444)
val color_critical_background_light = Color(0xffFEE2E2)
val color_critical_primary_dark = Color(0xffFFEDD5)
val color_critical_background_dark = Color(0xffFFEDD5)

@Composable
fun ColorScheme.toSuccess() = if (isLight) {
  lightColorScheme(
    primary = color_success_primary_light,
    background = color_success_background_light
  )
} else {
  MaterialTheme.colorScheme.toInfo()
  darkColorScheme(
    primary = color_success_primary_dark,
    background = color_success_background_dark
  )
}

@Composable
fun ColorScheme.toInfo() = if (isLight) {
  lightColorScheme(
    primary = color_info_primary_light,
    background = color_info_background_light
  )
} else {
  darkColorScheme(
    primary = color_info_primary_dark,
    background = color_info_background_dark
  )
}

@Composable
fun ColorScheme.toWarning() = if (isLight) {
  lightColorScheme(
    primary = color_warning_primary_light,
    background = color_warning_background_light
  )
} else {
  darkColorScheme(
    primary = color_warning_primary_dark,
    background = color_warning_background_dark
  )
}

@Composable
fun ColorScheme.toCritical() = if (isLight) {
  lightColorScheme(
    primary = color_critical_primary_light,
    background = color_critical_background_light
  )
} else {
  darkColorScheme(
    primary = color_critical_primary_dark,
    background = color_critical_background_dark
  )
}

@Composable
fun Difficulty.indicator(): Color {
  return when (this) {
    EASY -> if (isLight) color_level_easy_light else color_level_easy_dark
    MODERATE -> if (isLight) color_level_moderate_light else color_level_moderate_dark
    CHALLENGING -> if (isLight) color_level_challenging_light else color_level_challenging_dark
    DIFFICULT -> if (isLight) color_level_difficult_light else color_level_difficult_dark
    EXTREME -> if (isLight) color_level_extreme_light else color_level_extreme_dark
  }
}

val LocalColors: ProvidableCompositionLocal<ColorScheme> = compositionLocalOf { lightColorScheme() }

private val isLight: Boolean
  @Composable
  @ReadOnlyComposable
  get() = !isSystemInDarkTheme()

object Alpha {
  const val high: Float = 0.87f
  const val medium: Float = 0.60f
  const val disabled: Float = 0.38f
}

@Immutable
data class ExtendedColors(
  val surfaceContainer: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
  ExtendedColors(
    surfaceContainer = Color.Unspecified
  )
}

// TODO: Delete this
//val md_theme_light_surfaceVariant = Color(0xFFE1E4D5)
//val md_theme_light_onSurfaceVariant = Color(0xFF44483D)
//val md_theme_light_outline = Color(0xFF75796C)
//val md_theme_light_inverseOnSurface = Color(0xFFF2F1E9)
//val md_theme_light_inverseSurface = Color(0xFF30312C)
//val md_theme_light_inversePrimary = Color(0xFFA7D474)
//val md_theme_light_shadow = Color(0xFF000000)
//val md_theme_light_surfaceTint = Color(0xFF436915)
//val md_theme_light_outlineVariant = Color(0xFFC5C8BA)
//val md_theme_light_scrim = Color(0xFF000000)

//val color_error_light = Color(0xFFBA1A1A)
//val color_errorContainer_light = Color(0xFFFFDAD6)
//val color_onError_light = Color(0xFFFFFFFF)
//val color_onErrorContainer_light = Color(0xFF410002)

//val md_theme_dark_surfaceVariant = Color(0xFF44483D)
//val md_theme_dark_onSurfaceVariant = Color(0xFFC5C8BA)
//val md_theme_dark_outline = Color(0xFF8E9285)
//val md_theme_dark_inverseOnSurface = Color(0xFF1B1C18)
//val md_theme_dark_inverseSurface = Color(0xFFE3E3DB)
//val md_theme_dark_inversePrimary = Color(0xFF436915)
//val md_theme_dark_shadow = Color(0xFF000000)
//val md_theme_dark_surfaceTint = Color(0xFFA7D474)
//val md_theme_dark_outlineVariant = Color(0xFF44483D)
//val md_theme_dark_scrim = Color(0xFF000000)


//val md_theme_dark_tertiary = Color(0xFFA0CFCC)
//val md_theme_dark_onTertiary = Color(0xFF003735)
//val md_theme_dark_tertiaryContainer = Color(0xFF1F4E4B)
//val md_theme_dark_onTertiaryContainer = Color(0xFFBBECE8)
//val md_theme_dark_error = Color(0xFFFFB4AB)
//val md_theme_dark_errorContainer = Color(0xFF93000A)
//val md_theme_dark_onError = Color(0xFF690005)
//val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)

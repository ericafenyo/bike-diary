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

package com.ericafenyo.bikediary.libs.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

object Icons {

  val InformationCircle: Painter
    @Composable get() = painterResource(R.drawable.ic_information_circle)

  val ExclamationCircle: Painter
    @Composable get() = painterResource(R.drawable.ic_exclamation_circle)

  val Exclamation: Painter
    @Composable get() = painterResource(R.drawable.ic_exclamation)

  val CheckCircle: Painter
    @Composable get() = painterResource(R.drawable.ic_check_circle)

  val Close: Painter
    @Composable get() = painterResource(R.drawable.ic_close)

  val BrightnessMedium: Painter
    @Composable get() = painterResource(R.drawable.ic_brightness_medium)

  val ArrowDropDown: Painter
    @Composable get() = painterResource(R.drawable.ic_arrow_drop_down)

  val Add: Painter
    @Composable get() = painterResource(R.drawable.ic_add)

  val Remove: Painter
    @Composable get() = painterResource(R.drawable.ic_remove)

  val Bolt: Painter
    @Composable get() = painterResource(R.drawable.ic_bolt)

  val Fire: Painter
    @Composable get() = painterResource(R.drawable.ic_fire)

  val Map: Painter
    @Composable get() = painterResource(R.drawable.ic_map)

  val Eye: Painter
    @Composable get() = painterResource(R.drawable.ic_eye)

  val EyeOff: Painter
    @Composable get() = painterResource(R.drawable.ic_eye_off)

  val Camera: Painter
    @Composable get() = painterResource(R.drawable.ic_camera)

  val CurrentLocation: Painter
    @Composable get() = painterResource(R.drawable.ic_current_location)

  val Play: Painter
    @Composable get() = painterResource(R.drawable.ic_play)

  val User: Painter
    @Composable get() = painterResource(R.drawable.ic_user)

  val ChartPie: Painter
    @Composable get() = painterResource(R.drawable.ic_chart_pie)

  val Feed: Painter
    @Composable get() = painterResource(R.drawable.ic_feed)

  val ArrowLeft: Painter
    @Composable get() = painterResource(R.drawable.ic_arrow_left)
}

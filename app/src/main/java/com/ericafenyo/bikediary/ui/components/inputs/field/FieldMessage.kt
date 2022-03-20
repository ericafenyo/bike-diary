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

package com.ericafenyo.bikediary.ui.components.inputs.field

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.components.inputs.field.Message.Error
import com.ericafenyo.bikediary.ui.components.inputs.field.Message.Info

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FieldMessage(
  modifier: Modifier = Modifier,
  error: @Composable (() -> Unit)? = null,
  info: @Composable (() -> Unit)? = null,
) {
  val state = when {
    error != null -> Error(error)
    info != null -> Info(info)
    else -> null
  }

  AnimatedContent(targetState = state, transitionSpec = {
    if (targetState == null || initialState == null) {
      val enter = slideInVertically(animationSpec = tween(AnimationDuration)) + fadeIn(
        animationSpec = tween(AnimationDuration)
      )

      val exit = slideOutVertically(animationSpec = tween(AnimationDuration)) + fadeOut(
        animationSpec = tween(AnimationDuration)
      )

      val size = SizeTransform(clip = false) { _, _ -> tween(AnimationDuration) }

      // This uses kotlin infix function for composing animations
      enter with exit using size
    } else {
      val enter = fadeIn(animationSpec = tween(AnimationDuration))
      val exit = fadeOut(animationSpec = tween(AnimationDuration))
      val size = SizeTransform(clip = false) { _, _ -> tween(AnimationDuration) }

      // This uses kotlin infix function for composing animations
      enter with exit using size
    }
  }) { message: Message? ->
    if (message != null) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .padding(top = 6.dp)
          .then(modifier)
        ) {
        val (icon, tint) = when (message) {
          is Error -> Icons.Exclamation to MaterialTheme.colors.error
          is Info -> Icons.InformationCircle to Color.Blue
        }

        Icon(
          painter = icon,
          tint = tint,
          contentDescription = null,
          modifier = Modifier
            .padding(top = 2.dp, end = 4.dp)
            .size(16.dp),
        )

        message.content.invoke()
      }
    }
  }
}

private sealed class Message(
  open val content: @Composable () -> Unit
) {
  data class Error(override val content: @Composable () -> Unit) : Message(content)
  data class Info(override val content: @Composable () -> Unit) : Message(content)
}

private const val AnimationDuration = 150

@Composable
fun MergedTextStyle(value: TextStyle, content: @Composable () -> Unit) {
  ProvideTextStyle(value, content)
}
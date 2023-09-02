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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.theme.titleLarge
import com.ericafenyo.bikediary.ui.theme.titleMedium

@Composable
fun StepperModal(
  onAction: (Int) -> Unit,
  modifier: Modifier = Modifier,
  initialValue: Int = 0,
  step: Int = 1,
  min: Int = 0,
  max: Int = Int.MAX_VALUE,
  onDismiss: () -> Unit,
) {
  var inputValue by remember { mutableStateOf(initialValue) }

  fun increaseValue() {
    if (inputValue <= max) {
      inputValue += step
    }
  }

  fun decreaseValue() {
    if (inputValue >= min) {
      inputValue -= step
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    ConstraintLayout(
      modifier = modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp)
    ) {
      val (titleRef, increaseRef, decreaseRef, valueRef, buttonCancelRef, buttonSaveRef) = createRefs()

      Text(text = stringResource(id = string.profile_label_fitness_goal),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.constrainAs(titleRef) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        })

      createHorizontalChain(decreaseRef, valueRef, increaseRef)
      val barrier = createBottomBarrier(decreaseRef, valueRef, increaseRef)

      IconButton(
        onClick = { decreaseValue() },
        modifier = Modifier.constrainAs(decreaseRef) {
          top.linkTo(titleRef.bottom, 16.dp)
        }) {
        Icon(painter = Icons.Remove, contentDescription = "Decrease")
      }

      Text(
        text = "$inputValue",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.constrainAs(valueRef) {
          top.linkTo(decreaseRef.top)
          bottom.linkTo(decreaseRef.bottom)
        }
      )

      IconButton(
        onClick = { increaseValue() },
        modifier = Modifier.constrainAs(increaseRef) {
          top.linkTo(valueRef.top)
          bottom.linkTo(valueRef.bottom)
        }) {
        Icon(painter = Icons.Add, contentDescription = "Increase")
      }

      createHorizontalChain(buttonCancelRef, buttonSaveRef)

      TextButton(
        onClick = { },
        modifier = Modifier
          .padding(end = 8.dp)
          .constrainAs(buttonCancelRef) {
            top.linkTo(barrier)
            start.linkTo(parent.start)
            end.linkTo(buttonSaveRef.start)
            width = Dimension.fillToConstraints
          }
      ) {
        Text(text = "Cancel")
      }

      TextButton(
        onClick = { onAction.invoke(inputValue) },
        modifier = Modifier
          .padding(start = 8.dp)
          .constrainAs(buttonSaveRef) {
            top.linkTo(buttonCancelRef.top)
            bottom.linkTo(buttonCancelRef.bottom)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
          }
      ) {
        Text(text = "Save")
      }
    }
  }


}


@Preview
@Composable
fun StepperModalPreview() {
  AppTheme {
    StepperModal(
      initialValue = 6000,
      onDismiss = { },
      onAction = {}
    )
  }
}
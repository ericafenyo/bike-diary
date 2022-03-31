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

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ericafenyo.bikediary.model.Settings
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.titleMedium
import com.ericafenyo.bikediary.ui.components.inputs.TextField
import timber.log.Timber

@Composable
internal fun Profile(
  state: State<ProfileViewState>,
  dispatch: (ProfileAction) -> Unit,
) {
  var isDarkTheme by remember { mutableStateOf(false) }

  val settings = state.value.settings
  val isLoading = state.value.isLoading

  var showCaloriesDialog by remember { mutableStateOf(false) }
  var showDistanceDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .background(MaterialTheme.colors.background)
      .fillMaxSize()
      .padding(horizontal = 8.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    ThemeSwitcher(isChecked = isDarkTheme, onChecked = { value -> isDarkTheme = value })
    Spacer(modifier = Modifier.height(16.dp))
    FitnessGoals(
      calories = settings.quests.calories.target,
      distance = settings.quests.distance.target,
      changeCalories = { showCaloriesDialog = true },
      changeDistance = { showDistanceDialog = true },
    )
    if (showCaloriesDialog) {
      NumberPickerModel(
        title = "Calories per day",
        suffixText = "kcal",
        initialValue = settings.quests.calories.target,
        onDismiss = { showCaloriesDialog = false },
        onAction = { caloriesTarget ->
          Timber.d("THis : $caloriesTarget")
          val action = ProfileAction.UpdateSettings(
            Settings.Builder(settings).apply {
              calories = settings.quests.calories.copy(target = caloriesTarget.toInt())
            }.build()
          )
          dispatch(action)
        },
      )
    }

    if (showDistanceDialog) {
      NumberPickerModel(
        title = "Distance per day",
        suffixText = "km",
        initialValue = settings.quests.distance.target,
        onDismiss = { showDistanceDialog = false },
        onAction = { distanceTarget ->
          val builder = Settings.Builder(settings)
          builder.distance = settings.quests.distance.copy(target = distanceTarget.toDouble())
          val action = ProfileAction.UpdateSettings(builder.build())
          dispatch(action)
        },
      )
    }
  }
}

@Composable
private fun NumberPickerModel(
  title: String,
  suffixText: String,
  initialValue: Number,
  onAction: (Number) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var inputValue by remember { mutableStateOf("$initialValue") }

  Dialog(onDismissRequest = onDismiss) {
    Column(
      modifier = modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colors.surface)
        .padding(16.dp)
    ) {

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
      )

      Spacer(modifier = Modifier.height(16.dp))

      TextField(
        value = inputValue,
        onChange = { value -> inputValue = value },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        trailingIcon = { Text(text = suffixText) }
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .offset(x = 8.dp, y = 16.dp),
        horizontalArrangement = Arrangement.End
      ) {

        TextButton(
          onClick = { onDismiss.invoke() },
        ) {
          Text(text = "Cancel")
        }

        TextButton(
          onClick = { onAction.invoke(inputValue.toDouble()).also { onDismiss() } }
        ) {
          Text(text = "Save")
        }
      }
    }
  }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProfilePreview() {
  AppTheme {
    NumberPickerModel(
      initialValue = 500,
      onDismiss = {},
      onAction = {},
      title = "Calories per day",
      suffixText = "kcal"
    )
  }
}

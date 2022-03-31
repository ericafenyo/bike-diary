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

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.titleMedium

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FitnessGoals(
  calories: Int = 0,
  distance: Double = 0.0,
  changeCalories: () -> Unit,
  changeDistance: () -> Unit,
) {
  Card(modifier = Modifier.fillMaxWidth(), elevation = 0.dp) {
    ConstraintLayout(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      val (titleRef, distanceRef, caloriesRef) = createRefs()
      val centerGuideline = createGuidelineFromStart(fraction = 0.5f)

      Text(text = stringResource(id = string.profile_label_fitness_goal),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.constrainAs(titleRef) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
        })

      ExposedDropdownMenuBox(
        expanded = false,
        onExpandedChange = { changeDistance.invoke() },
        modifier = Modifier.constrainAs(distanceRef) {
          top.linkTo(titleRef.bottom, 16.dp)
          start.linkTo(parent.start)
          end.linkTo(centerGuideline, 8.dp)
          width = Dimension.fillToConstraints
        }
      ) {
        OutlinedTextField(
          value = String.format("%.1f", distance),
          onValueChange = {},
          readOnly = true,
          label = { Text(text = "Distance") },
          trailingIcon = { Icon(painter = Icons.ArrowDropDown, contentDescription = null) }
        )
      }

      ExposedDropdownMenuBox(
        expanded = false,
        onExpandedChange = { changeCalories.invoke() },
        modifier = Modifier.constrainAs(caloriesRef) {
          top.linkTo(distanceRef.top)
          start.linkTo(centerGuideline, 8.dp)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
        }
      ) {
        OutlinedTextField(
          value = "$calories",
          onValueChange = {},
          readOnly = true,
          label = { Text(text = "Clories") },
          trailingIcon = { Icon(painter = Icons.ArrowDropDown, contentDescription = null) }
        )
      }
    }
  }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FitnessGoalsPreview() {
  AppTheme {
    FitnessGoals(
      calories = 500,
      distance = 1000.0,
      changeCalories = { },
      changeDistance = {},
    )
  }
}

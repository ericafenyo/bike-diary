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

package com.ericafenyo.bikediary.ui.screens.dashboard

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.R.drawable
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.model.Quests
import com.ericafenyo.bikediary.ui.components.ToolBar
import com.ericafenyo.bikediary.ui.theme.AppTheme

@Composable
fun DashboardContent(viewModel: DashboardViewModel = hiltViewModel()) {
  Dashboard(viewModel)
}

@Composable
fun Dashboard(viewModel: DashboardViewModel) {
  Scaffold(
    topBar = { ToolBar(title = stringResource(id = R.string.title_dashboard)) }
  ) { contentPadding ->
    contentPadding

    val state by viewModel.state.collectAsState()
    val context: Context = LocalContext.current
  }
}

@Composable
private fun LevelBadge(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    onClick = onClick
  ) {
    ConstraintLayout(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      val (imageBadge, textNextLevel, progressBar, textDistanceRef, textDistanceLeftRef) = createRefs()

      Icon(
        painter = painterResource(id = drawable.badge_level_one),
        contentDescription = null,
        modifier = Modifier
          .size(52.dp)
          .constrainAs(imageBadge) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
          }
      )

      Text(
        text = "Level 2",
        style = MaterialTheme.typography.titleMedium,

        modifier = Modifier.constrainAs(textNextLevel) {
          start.linkTo(imageBadge.end, 16.dp)
          bottom.linkTo(progressBar.top, 16.dp)
        }
      )

      LinearProgressIndicator(
        progress = 0.3f,
        modifier = Modifier
          .height(6.dp)
          .constrainAs(progressBar) {
            start.linkTo(imageBadge.end, 16.dp)
            bottom.linkTo(imageBadge.bottom)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
          }
      )

      Text(
        text = "53.0",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .padding(end = 8.dp)
          .constrainAs(textDistanceRef) {
            baseline.linkTo(textNextLevel.baseline)
            end.linkTo(textDistanceLeftRef.start)
          }
      )
      Text(text = "km left", modifier = Modifier.constrainAs(textDistanceLeftRef) {
        end.linkTo(parent.end)
        baseline.linkTo(textDistanceRef.baseline)
      })
    }
  }
}


@Composable
fun DailyQuests(
  quests: Quests,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
  ) {
    Text(
      text = "Daily quests",
      style = MaterialTheme.typography.titleSmall,
      color = Color(0xff6b7280)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
      Box(
        modifier = Modifier
          .weight(1f)
          .padding(end = 4.dp)
      ) {
        DailyQuestItem(
          label = "Calories",
          progress = 0.6f,
          target = "1000",
          color = Color(0xffeab308),
          background = Color(0xfffef3c7),
          completed = "459",
          percentage = "48%",
          icon = Icons.Fire
        )
      }

      Box(
        modifier = Modifier
          .weight(1f)
          .padding(start = 4.dp)
      ) {
        DailyQuestItem(
          label = "Distance",
          progress = 0.3f,
          target = "1000",
          completed = "459",
          color = Color(0xff3b82f6),
          background = Color(0xffdbeafe),
          percentage = "48%",
          icon = Icons.Map
        )
      }
    }
  }
}


@Composable
fun DailyQuestItem(
  icon: Painter,
  label: String,
  progress: Float,
  color: Color,
  background: Color,
  target: String,
  completed: String,
  percentage: String,
  modifier: Modifier = Modifier
) {
  Card() {
    Column(modifier = modifier.padding(8.dp)) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(MaterialTheme.shapes.small)
          .background(background)
      ) {
        Icon(
          painter = icon,
          contentDescription = null,
          tint = color,
          modifier = Modifier
            .align(Alignment.Center)
            .padding(6.dp)
        )
      }
      Text(
        text = label,
        modifier = Modifier
          .padding(vertical = 16.dp),
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xff111827)
      )
      LinearProgressIndicator(
//        backgroundColor = background,
        color = color,
        progress = progress
      )

      Row(modifier = Modifier.padding(top = 4.dp)) {
        Text(
          text = "$completed/$target",
          style = MaterialTheme.typography.labelMedium,
          color = Color(0xff6b7280)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
          text = percentage,
          style = MaterialTheme.typography.labelMedium,
          color = Color(0xff6b7280)
        )
      }
    }
  }
}

@Preview
@Composable
fun DashboardPreview() {
  AppTheme {
    DashboardContent()
  }
}

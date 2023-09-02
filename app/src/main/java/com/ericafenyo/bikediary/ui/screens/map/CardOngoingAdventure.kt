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

package com.ericafenyo.bikediary.ui.screens.map

import  androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.R.string
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.model.Distance
import com.ericafenyo.bikediary.model.Energy
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.tracker.Tracker
import com.ericafenyo.tracker.Tracker.State.IDLE
import com.ericafenyo.tracker.Tracker.State.ONGOING
import com.ericafenyo.tracker.Tracker.State.PAUSE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@Composable
fun CardOngoingAdventure(
  modifier: Modifier = Modifier,
  time: String = "01 : 20 : 05",
  distance: Distance = Distance.kilometers(24.0),
  energy: Energy = Energy.calories(1050.0),
  trackerState: Tracker.State,
  dispatch: (action: TrackerAction) -> Unit
) {
  Card(
    elevation = CardDefaults.elevatedCardElevation(),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    modifier = modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.large)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = time,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "${distance.kilometers} km - ${energy.calories} cal",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 24.dp)
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        LockButton { dispatch(TrackerAction.TOGGLE_LOCK) }
        when (trackerState) {
          IDLE -> StartButton { dispatch(TrackerAction.START) }
          ONGOING -> PauseButton { dispatch(TrackerAction.PAUSE) }
          PAUSE -> {
            StartButton { dispatch(TrackerAction.RESUME) }
            StopButton { dispatch(TrackerAction.STOP) }
          }
        }
        CameraButton { dispatch(TrackerAction.OPEN_CAMERA) }
      }
    }
  }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
  FloatingActionButton(
    onClick = onClick,
    shape = CircleShape,
    containerColor = MaterialTheme.colorScheme.primary,
    modifier = Modifier.size(80.dp),
    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
  ) {
    Icon(
      painter = Icons.Play,
      contentDescription = stringResource(id = string.map_a11y_start_adventure),
      tint = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.size(34.dp),
    )
  }
}

@Composable
private fun StopButton(onClick: () -> Unit) {
  FloatingActionButton(
    onClick = onClick,
    shape = CircleShape,
    containerColor = MaterialTheme.colorScheme.errorContainer,
    modifier = Modifier.size(80.dp),
    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
  ) {
    Icon(
      painter = Icons.Stop,
      contentDescription = stringResource(id = string.map_a11y_start_adventure),
      tint = MaterialTheme.colorScheme.onErrorContainer,
      modifier = Modifier.size(34.dp),
    )
  }
}

@Composable
private fun PauseButton(onClick: () -> Unit) {
  FloatingActionButton(
    onClick = onClick,
    shape = CircleShape,
    containerColor = MaterialTheme.colorScheme.primary,
    modifier = Modifier.size(80.dp),
    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
  ) {
    Icon(
      painter = Icons.Pause,
      contentDescription = stringResource(id = string.map_a11y_start_adventure),
      tint = MaterialTheme.colorScheme.onPrimary,
      modifier = Modifier.size(34.dp),
    )
  }
}

@Composable
private fun CameraButton(onClick: () -> Unit) {
  FloatingActionButton(
    onClick = onClick,
    shape = CircleShape,
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    modifier = Modifier.size(48.dp),
    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
  ) {
    Icon(
      painter = Icons.Camera,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSecondaryContainer,
    )
  }
}

@Composable
private fun LockButton(onClick: () -> Unit) {
  FloatingActionButton(
    onClick = onClick,
    shape = CircleShape,
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    modifier = Modifier.size(48.dp),
    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
  ) {
    Icon(
      painter = Icons.Lock,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSecondaryContainer,
    )
  }
}

@Preview
@Composable
private fun CardOngoingAdventurePreview() {
  AppTheme(useDarkTheme = false) {
    BoxWithConstraints {
      CardOngoingAdventure(
        modifier = Modifier,
        time = "01 : 20 : 05",
        distance = Distance.kilometers(24.0),
        energy = Energy.calories(1050.0),
        trackerState = Tracker.State.ONGOING,
        dispatch = {}
      )
    }
  }
}

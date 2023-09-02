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

package com.ericafenyo.bikediary.ui.screens.map

import  android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.ericafenyo.bikediary.Constants
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.components.LoadingState
import com.ericafenyo.bikediary.ui.components.dialog.FullScreenDialog
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.OPEN_CAMERA
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.PAUSE
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.RESUME
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.START
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.STOP
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.TOGGLE_LOCK
import com.ericafenyo.bikediary.ui.screens.photo.AddPhoto
import com.ericafenyo.tracker.Tracker
import com.ericafenyo.tracker.timer.StopWatch
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mapbox.maps.MapView
import java.io.File
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@Composable
fun MapContent(viewModel: TrackerViewModel = hiltViewModel()) {
  Map(viewModel)
}

@Composable
fun Map(viewModel: TrackerViewModel) {
  when (val state: TrackerUiState = viewModel.state.collectAsState().value) {
    is TrackerUiState.Loading -> LoadingState()
    is TrackerUiState.Success -> Map(viewModel, state.trackerState)
  }
}

@Composable
fun Map(viewModel: TrackerViewModel, trackerState: Tracker.State) {
  val context: Context = LocalContext.current
  var showAddPhotoDialog by remember { mutableStateOf(false) }
  var options: Options by remember { mutableStateOf(Options.EMPTY) }
  var duration: Long by remember { mutableLongStateOf(0L) }
  val watch = StopWatch.getInstance()

  watch.setListener {
    duration = it
  }

  val cameraRequest = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture(),
    onResult = { success ->
      // The photo has being saved.Display the photo
      showAddPhotoDialog = true
//      val location = getLocation(context)

      Timber.tag("DEBUGGING_LOG").i("Device photo successful: $success")
    }
  )

  val cameraPermissionRequest = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { granted ->
      if (granted) {
        val fileOptions = generateOptions(context)
        options = fileOptions
        cameraRequest.launch(fileOptions.uri)
      }
    }
  )
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    if (showAddPhotoDialog) {
      FullScreenDialog(title = stringResource(id = R.string.title_add_photo), onDismiss = {
        showAddPhotoDialog = false
      }) {
        AddPhoto(name = options.name, uri = options.uri, file = options.file)
      }
    }

    MapboxView()

    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
      val time = watch.format(duration)

        Speedometer()
      CardOngoingAdventure(
        modifier = Modifier.padding(16.dp),
        time = time,
        dispatch = { action ->
          when (action) {
            OPEN_CAMERA -> {
              cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }

            PAUSE -> viewModel.dispatch(action)
            RESUME -> viewModel.dispatch(action)
            START -> viewModel.dispatch(action)
            STOP -> viewModel.dispatch(action)
            TOGGLE_LOCK -> {}
          }
        },
        trackerState = trackerState
      )
    }
  }
}


@Composable
fun Speedometer() {
  Card(
    shape = CircleShape,
    elevation = CardDefaults.elevatedCardElevation(),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
    modifier = Modifier
      .padding(16.dp)
      .size(80.dp),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = "8",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onTertiary
      )
      Text(
        text = "km/h",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onTertiary
      )
    }
  }
}


@SuppressLint("MissingPermission")
fun getLocation(context: Context) {
  val services = LocationServices.getFusedLocationProviderClient(context)
  val request = CurrentLocationRequest.Builder()
    .setGranularity(Granularity.GRANULARITY_FINE)
    .setMaxUpdateAgeMillis(0)
    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
    .build()

  services.getCurrentLocation(request, null)
    .addOnSuccessListener { location ->
      Timber.tag("DEBUGGING_LOG").i("This is the photo location: $location")
    }.addOnFailureListener { exception ->
      Timber.tag("DEBUGGING_LOG").e(exception, "This is the failed photo location:")
    }
}

@Composable
fun MapboxView(modifier: Modifier = Modifier) {
  val mapView = rememberMapView()
  AndroidView(factory = { mapView }, modifier = modifier)
}

@Composable
fun rememberMapView(): MapView {
  val context = LocalContext.current
  return remember { MapView(context) }
}

fun generateOptions(context: Context): Options {
  val folder = File("${context.filesDir}/photos")
  if (!folder.exists()) {
    folder.mkdirs()
  }

  val name = UUID.randomUUID()
  val file = File("$folder/$name")
  val uri = FileProvider.getUriForFile(context, Constants.FILE_PROVIDER_AUTHORITY, file)

  return Options(
    file = file,
    name = name,
    uri = uri,
  )
}

data class Options(
  val name: UUID,
  val file: File,
  val uri: Uri,
) {
  companion object {
    val EMPTY = Options(
      name = UUID.randomUUID(),
      file = File(""),
      uri = Uri.EMPTY
    )
  }
}

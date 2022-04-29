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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.bodyLarge
import com.ericafenyo.bikediary.theme.contentMedium
import com.ericafenyo.bikediary.theme.displayMedium
import com.mapbox.maps.MapView


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Map() {
  Box(modifier = Modifier.fillMaxSize()) {
    val sheetState = rememberBottomSheetScaffoldState(
      bottomSheetState = BottomSheetState(BottomSheetValue.Expanded)
    )
    BottomSheetScaffold(
      sheetShape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomEnd = 0.dp,
        bottomStart = 0.dp
      ),
      scaffoldState = sheetState,
      sheetContent = {
        TrackingMetrics()
      },
      sheetGesturesEnabled = false,
      floatingActionButton = {
        FloatingActionButton(
          modifier = Modifier.offset(y = (-34).dp),
          onClick = { /*TODO*/ },
          backgroundColor = Color.White,
        ) {
          Icon(
            tint = MaterialTheme.colors.primary,
            painter = Icons.CurrentLocation,
            contentDescription = null
          )
        }
      },
      floatingActionButtonPosition = FabPosition.End
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        MapboxView()
      }
    }
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

@Composable
fun TrackingMetrics(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
  ) {
    Column {
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        IconButton(onClick = {

        }, modifier = Modifier.padding(16.dp)) {
          Icon(
            painter = Icons.Close, contentDescription = "End adventure"
          )
        }
        Text(
          text = "01:32:56",
          color = MaterialTheme.colors.primary,
          style = MaterialTheme.typography.displayMedium
        )
        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(16.dp)) {
          Icon(painter = Icons.Camera, contentDescription = "Take Photo")
        }
      }

      Text(
        text = "2.6 km . 156 kcal",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        color = MaterialTheme.colors.contentMedium
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Preview
@Composable
fun TrackingMetricsPreview() {
  AppTheme {
    Map()
  }
}

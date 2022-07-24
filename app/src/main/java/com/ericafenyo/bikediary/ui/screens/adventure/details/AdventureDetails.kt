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

package com.ericafenyo.bikediary.ui.screens.adventure.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.headlineSmall
import com.ericafenyo.bikediary.ui.screens.adventure.details.AdventureDetailsUiState.Loading
import com.ericafenyo.bikediary.ui.screens.adventure.details.AdventureDetailsUiState.Success
import java.time.Duration

@Composable
fun AdventureDetailsContent(
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: AdventureDetailsViewModel = hiltViewModel()
) {
  AppTheme {
    AdventureDetails(onBackPressed, modifier, viewModel)
  }
}


@Composable
fun AdventureDetails(
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: AdventureDetailsViewModel
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(text = "Details") },
        navigationIcon = {
          IconButton(onClick = onBackPressed) {
            Icon(painter = Icons.ArrowLeft, contentDescription = null)
          }
        },
        backgroundColor = MaterialTheme.colors.surface
      )
    }
  ) {
    val state by viewModel.state.collectAsState()

    when (state) {
      is Loading -> {
        Text(text = "Loading...")
      }
      is Success -> {
        val adventure = (state as Success).adventure
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
        ) {
          Column {
            AsyncImage(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1F),
              model = "https://d2ucnymlzowcof.cloudfront.net/${adventure.image}",
              contentDescription = null,
              contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = adventure.title,
              style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
              Text(text = "${adventure.distance}m - ")
              Text(text = "${adventure.altitude}m - ")
              Text(text = Duration.ofSeconds(adventure.duration.toLong()).toString())
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = adventure.description)
          }
        }
      }
    }
  }
}

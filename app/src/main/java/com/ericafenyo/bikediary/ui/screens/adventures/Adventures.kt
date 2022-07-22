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

package com.ericafenyo.bikediary.ui.screens.adventures

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ericafenyo.bikediary.theme.contentHigh
import com.ericafenyo.bikediary.theme.titleLarge
import com.ericafenyo.bikediary.ui.components.LoadingState
import com.ericafenyo.bikediary.ui.screens.adventures.AdventureUiState.Loading
import com.ericafenyo.bikediary.ui.screens.adventures.AdventureUiState.Success

@Composable
fun AdventuresContent(viewModel: AdventureViewModel = hiltViewModel()) {
  Adventures(viewModel)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Adventures(viewModel: AdventureViewModel) {
  val state by viewModel.state.collectAsState()
  val context = LocalContext.current

  Scaffold(
    topBar = {
      val titleStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Medium
      )

      TopAppBar(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
      ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
          Text(
            text = "Adventures",
            style = titleStyle,
            color = MaterialTheme.colors.contentHigh
          )
        }
      }
    }
  ) { paddingValues ->
    val isLoading = state is Loading

    AnimatedContent(targetState = isLoading) { targetEmpty ->
      if (targetEmpty) {
        LoadingState()
      } else {
        when (state) {
          is Success -> {
            LazyColumn(
              contentPadding = paddingValues,
              modifier = Modifier.fillMaxWidth()
            ) {
              (state as Success).adventures.forEach { adventure ->

                item {
                  AdventureItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    metrics = "${adventure.distance} - ${adventure.duration}",
                    title = adventure.title,
                    time = "${adventure.startTime} - ${adventure.endTime}",
                    image = loadImage(
                      context = context,
                      key = adventure.image,
                      viewModel = viewModel
                    ).value
                  )
                }
              }
            }
          }
          else -> {}
        }
      }
    }
  }
}

@Composable
fun loadImage(
  context: Context,
  key: String,
  viewModel: AdventureViewModel
): State<Drawable?> {
  return produceState<Drawable?>(initialValue = null) {
    value = viewModel.loadImage(context, key)
  }
}

@Composable
fun AdventureItem(
  title: String,
  time: String,
  metrics: String,
  image: Drawable?,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RectangleShape,
    modifier = modifier,
    elevation = 0.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      AsyncImage(
        model = image,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
      )
      Box(
        modifier = Modifier
          .padding(16.dp)
          .clip(MaterialTheme.shapes.large)
      ) {
        Column {
          Text(text = time)
          Text(text = title)
          Text(text = metrics)
        }
      }
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

sealed class UIState {
  object Loading : UIState()
  object Success : UIState()
  object Error : UIState()
  object Empty : UIState()
}

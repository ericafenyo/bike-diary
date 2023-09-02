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

package com.ericafenyo.bikediary.ui.screens.adventure

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.R.drawable
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.theme.titleLarge
import com.ericafenyo.bikediary.ui.components.LoadingState
import com.ericafenyo.bikediary.ui.screens.adventure.AdventureUiState.Loading
import com.ericafenyo.bikediary.ui.screens.adventure.AdventureUiState.Success

@Composable
fun AdventuresContent(
  modifier: Modifier = Modifier,
  navigateToDetails: (String) -> Unit,
  viewModel: AdventureViewModel = hiltViewModel()
) {
  Adventures(modifier, navigateToDetails, viewModel)
}

@Composable
fun Adventures(
  modifier: Modifier,
  navigateToDetails: (String) -> Unit,
  viewModel: AdventureViewModel
) {
  val state by viewModel.state.collectAsState()
  val context = LocalContext.current

  Scaffold(
    modifier = modifier
      .padding(horizontal = 8.dp)
    ,
    topBar = {
      val titleStyle = MaterialTheme.typography.titleLarge
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
          Text(
            text = "Adventures",
            style = titleStyle,
          )
        }
      )
    }
  ) { paddingValues ->
    val isLoading = state is Loading

    AnimatedContent(targetState = isLoading, label = "") { targetEmpty ->
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
//                  AdventureItem(
//                    modifier = Modifier.padding(vertical = 4.dp),
//                    distance = adventure.distance,
//                    title = adventure.title,
//                    location = "Nantes",
//                    image = "https://plus.unsplash.com/premium_photo-1670963025394-5cd71a3ae967?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=987&q=80",
//                    onClick = navigateToDetails
//                  )
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

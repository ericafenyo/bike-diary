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

package com.ericafenyo.bikediary.ui.screens.photo

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.R.drawable
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.components.inputs.TextField
import com.ericafenyo.bikediary.ui.components.buttons.Button
import java.io.File
import java.util.UUID

@Composable
fun AddPhoto(
  uri: Uri,
  name: UUID,
  file: File,
  viewModel: PhotosViewModel = hiltViewModel()
) {
  val context: Context = LocalContext.current
  var caption by remember { mutableStateOf("") }

  Scaffold { paddings ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface)
        .padding(paddings)
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        PhotoView(uri)

        TextField(
          value = caption,
          onChange = { caption = it},
          label = stringResource(id = R.string.label_caption),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
        )
        Button(
          text = stringResource(id = R.string.action_save),
          onClick = { viewModel.savePhoto(name, caption) },
          isLoading = viewModel.savingIndicator.collectAsState().value.loading,
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
            .clip(MaterialTheme.shapes.extraSmall)
        )
      }
    }
  }
}

@Composable
fun PhotoView(uri: Uri) {
  val request = ImageRequest.Builder(LocalContext.current)
    .data(uri)
    .build()

  Box(
    modifier = Modifier
      .aspectRatio(4F / 3F)
      .fillMaxWidth()
  ) {
    AsyncImage(
      model = request,
      placeholder = painterResource(id = drawable.photo),
      contentDescription = null,
      contentScale = ContentScale.Crop
    )
  }
}

@Preview
@Composable
fun AddPhotoPreview() {
  AppTheme {
    AddPhoto(uri = Uri.EMPTY, name= UUID.randomUUID(), File("/"))
  }
}

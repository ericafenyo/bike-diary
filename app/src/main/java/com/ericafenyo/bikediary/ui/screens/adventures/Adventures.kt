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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.contentHigh
import com.ericafenyo.bikediary.theme.titleLarge

@Composable
fun AdventuresContent(viewModel: AdventureViewModel = viewModel()) {
  Adventures(viewModel)
}

@Composable
fun Adventures(viewModel: AdventureViewModel) {
  val state = viewModel.state

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

    LazyColumn(
      contentPadding = paddingValues,
      modifier = Modifier.fillMaxWidth()
    ) {
      repeat(10) {
        item {
//          AdventureItem(modifier = Modifier.padding(vertical = 4.dp))
        }
      }
    }
  }
}


@Composable
fun AdventureItem(
  title: String,
  time: String,
  metrics: String,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RectangleShape,
    modifier = modifier,
    elevation = 0.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
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

@Composable
@Preview()
fun MetricItemPreview() {
  AppTheme {
    AdventureItem(
      title = "NP - Côte de la fosse Garreau",
      time = "7h43 - 13h34",
      metrics = "23km - 63h"
    )
  }
}

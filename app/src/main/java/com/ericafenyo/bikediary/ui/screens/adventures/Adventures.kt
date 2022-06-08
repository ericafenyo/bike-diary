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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.theme.Alpha
import com.ericafenyo.bikediary.theme.AppTheme
import com.ericafenyo.bikediary.theme.bodyMedium
import com.ericafenyo.bikediary.theme.contentHigh
import com.ericafenyo.bikediary.theme.contentMedium
import com.ericafenyo.bikediary.theme.labelMedium
import com.ericafenyo.bikediary.theme.titleLarge
import com.ericafenyo.bikediary.theme.titleMedium

@Composable
fun AdventuresContent() {
  Text(text = "Adventures")
}

@Composable
fun Adventures(

) {
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
          AdventureItem(modifier = Modifier.padding(vertical = 4.dp))
        }
      }
    }
  }
}


@Composable
fun AdventureItem(modifier: Modifier = Modifier) {
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
        AsyncImage(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
          model = ImageRequest.Builder(LocalContext.current)
            .data("https://images.unsplash.com/photo-1452421822248-d4c2b47f0c81?w=400")
            .crossfade(true)
            .build(),
          contentScale = ContentScale.Crop,
          contentDescription = null
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        MetricItem(
          backgroundColor = Color.Red.copy(Alpha.disabled),
          color = Color.Red,
          label = "Speed",
          unit = "km/h",
          value = "50",
          icon = Icons.Bolt
        )

        MetricItem(
          backgroundColor = Color.Red.copy(Alpha.disabled),
          color = Color.Red,
          label = "Distance",
          unit = "km/h",
          value = "50",
          icon = Icons.Map
        )

        MetricItem(
          backgroundColor = Color.Red.copy(Alpha.disabled),
          color = Color.Red,
          label = "Duration",
          unit = "km/h",
          value = "50",
          icon = Icons.Bolt
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
fun MetricItem(
  backgroundColor: Color,
  color: Color,
  icon: Painter,
  label: String,
  unit: String,
  value: String
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape)
        .background(backgroundColor)
    ) {
      Icon(
        painter = icon, contentDescription = null,
        modifier = Modifier
          .align(Alignment.Center)
          .size(16.dp),
        tint = color
      )
    }

    Spacer(modifier = Modifier.width(8.dp))

    Column {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colors.contentMedium
      )
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = value,
          style = MaterialTheme.typography.titleMedium.copy(
            lineHeight = 1.sp
          ),
          color = MaterialTheme.colors.contentHigh,
        )
        Text(
          text = unit,
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colors.contentMedium,
        )
      }
    }
  }
}

@Composable
@Preview()
fun MetricItemPreview() {
  AppTheme {
    Adventures()
  }
}

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

package com.ericafenyo.bikediary.ui.screens.adventure

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ericafenyo.bikediary.R.drawable
import com.ericafenyo.bikediary.model.Address
import com.ericafenyo.bikediary.model.Difficulty
import com.ericafenyo.bikediary.model.Difficulty.EASY
import com.ericafenyo.bikediary.model.Difficulty.EXTREME
import com.ericafenyo.bikediary.model.Distance
import com.ericafenyo.bikediary.ui.theme.AppTheme
import com.ericafenyo.bikediary.ui.theme.indicator
import com.ericafenyo.bikediary.util.label
import java.time.Duration

@Composable
fun AdventureItem(
  modifier: Modifier = Modifier,
  photo: String,
  difficulty: Difficulty,
  distance: Distance,
  duration: Duration,
  address: Address,
  onClick: (String) -> Unit
) {
  val context = LocalContext.current
  val indicatorColor = difficulty.indicator()
  val imageRequest = ImageRequest.Builder(context)
    .data(photo)
    .build()

  Box(
    modifier = modifier
      .border(BorderStroke(1.dp, color = indicatorColor.copy(alpha = 0.10F)))
      .clip(MaterialTheme.shapes.extraLarge)
      .background(
        color = indicatorColor.copy(alpha = 0.08F),
      )
  ) {

    Column {
      AsyncImage(
        model = imageRequest,
        placeholder = painterResource(id = drawable.photo),
        contentDescription = null,
        modifier = Modifier
          .aspectRatio(4F / 3F)
          .clip(MaterialTheme.shapes.extraLarge),

        contentScale = ContentScale.Crop
      )

      Column(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .padding(top = 16.dp, bottom = 24.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(12.dp)
              .clip(CircleShape)
              .background(color = indicatorColor)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            style = MaterialTheme.typography.labelMedium,
            text = "${difficulty.label(context)} - ${distance.kilometers} - Est. ${duration.toMinutes()} m"
          )

          Text(text = "4.6", style = MaterialTheme.typography.labelMedium)
        }
        Text(
          text = "Turahalli Forest MTB Trail",
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(top = 8.dp)
        )

        Box(modifier = Modifier) {
          Row {
            Text(
              text = "3.7km - Nantes",
              style = MaterialTheme.typography.bodySmall,
            )
          }
        }
      }
    }

  }
}


@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Composable
fun AdventureItemPreview() {
  BoxWithConstraints(
    modifier = Modifier
      .background(color = Color.White)
      .padding(horizontal = 16.dp, vertical = 24.dp)
  ) {
    AppTheme(useDarkTheme = false) {
      AdventureItem(
        photo = "",
        difficulty = EXTREME,
        distance = Distance.kilometers(23.6),
        address = Address(
          city = "La Chapelle-Heulin",
          region = "Loire-Atlantique",
          country = "France",
          countryCode = "FR"
        ),
        duration = Duration.ofMinutes(59),
        onClick = {}
      )
    }
  }
}

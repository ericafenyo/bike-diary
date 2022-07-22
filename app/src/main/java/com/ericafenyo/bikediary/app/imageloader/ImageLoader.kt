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

package com.ericafenyo.bikediary.app.imageloader

import coil.ImageLoader
import android.content.Context
import coil.disk.DiskCache
import coil.request.ImageRequest

object ImageLoaderManager {

  fun getCachedImageLoader(context: Context): ImageLoader {

    val cache = DiskCache.Builder()
      .directory(context.cacheDir.resolve("photos"))
      .maxSizePercent(1.0)
      .build()

    return ImageLoader.Builder(context)
      .diskCache(cache)
      .build()
  }

  fun createRequest(context: Context, fileUrl: String, key: String): ImageRequest {
    return ImageRequest.Builder(context)
      .data(fileUrl)
      .diskCacheKey(key)
      .build()
  }
}
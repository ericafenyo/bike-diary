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

package com.ericafenyo.bikediary.repositories.photo.internal

import com.ericafenyo.bikediary.database.entity.PhotoEntity
import com.ericafenyo.bikediary.model.Photo
import com.ericafenyo.bikediary.repositories.photo.PhotoLocalDataSource
import com.ericafenyo.bikediary.repositories.photo.PhotoRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepositoryImpl @Inject constructor(
  private val localDataSource: PhotoLocalDataSource
) : PhotoRepository {
  override suspend fun getPhoto(id: UUID): Photo {
    TODO("Not yet implemented")
  }

  override suspend fun savePhoto(photo: Photo) {
    localDataSource.insert(photo.toEntity())
  }

  private fun Photo.toEntity(): PhotoEntity = PhotoEntity(
    id = id,
    caption = caption,
    hash = hash,
    coordinate = coordinate,
    timestamp = timestamp
  )

  private fun PhotoEntity.toPhoto(): Photo = Photo(
    id = id,
    caption = caption,
    hash = hash,
    coordinate = coordinate,
    timestamp = timestamp
  )
}


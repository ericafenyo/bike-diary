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

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.app.LocationProvider
import com.ericafenyo.bikediary.domain.photo.SavePhotoInteractor
import com.ericafenyo.bikediary.model.Coordinate
import com.ericafenyo.bikediary.model.Photo
import com.ericafenyo.bikediary.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class PhotosViewModel @Inject constructor(
  private val locationProvider: LocationProvider,
  private val savePhotoInteractor: SavePhotoInteractor,
) : ViewModel() {

  private val _savingIndicator = MutableStateFlow(UIState())
  val savingIndicator: StateFlow<UIState> = _savingIndicator.asStateFlow()

  /**
   * Saves a [Photo] object to the database.
   *
   * @param caption the description of the photo
   */
  fun savePhoto(name: UUID, caption: String) {
    viewModelScope.launch {
      _savingIndicator.value = UIState(loading = true)
      val location: Location = locationProvider.getCurrentLocation()
      val photo = Photo(
        id = name,
        caption = caption,
        hash = name.toString(),
        coordinate = Coordinate(latitude = location.latitude, longitude = location.longitude),
        timestamp = Instant.now()
      )

      savePhotoInteractor.invoke(photo).also {
        _savingIndicator.value = UIState(loading = false)
      }
    }
  }
}

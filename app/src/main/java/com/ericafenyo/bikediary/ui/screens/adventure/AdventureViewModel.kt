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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import com.ericafenyo.bikediary.app.imageloader.ImageLoaderManager
import com.ericafenyo.bikediary.domain.adventure.AdventuresInteractor
import com.ericafenyo.bikediary.domain.adventure.UpdateAdventuresInteractor
import com.ericafenyo.bikediary.model.Adventure
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AdventureViewModel @Inject constructor(
  private val updateAdventuresInteractor: UpdateAdventuresInteractor,
  adventuresInteractor: AdventuresInteractor,
  private val imageLoader: ImageLoader
) : ViewModel() {
  private val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

  val state: StateFlow<AdventureUiState> = adventuresInteractor.observe()
    .map { adventures ->
      if (adventures.isEmpty()) {
        AdventureUiState.Empty
      } else {
        AdventureUiState.Success(adventures)
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.Eagerly,
      initialValue = AdventureUiState.Loading
    )

  suspend fun loadImage(context: Context, imagePath: String): Drawable? {
    val url = "https://d2ucnymlzowcof.cloudfront.net/$imagePath"
    val request = ImageLoaderManager.createRequest(context, url, imagePath)
    return imageLoader.execute(request).drawable
  }

  init {
    viewModelScope.launch {
      isLoading.value = true
      updateAdventuresInteractor.invoke().run {
        isLoading.value = false
      }
    }
  }
}

sealed class AdventureUiState {
  object Loading : AdventureUiState()
  object Empty : AdventureUiState()
  data class Success(val adventures: List<Adventure> = emptyList()) : AdventureUiState()
}

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

package com.ericafenyo.bikediary.ui.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.data.settings.ObserveThemeInteractor
import com.ericafenyo.bikediary.data.settings.SetThemeUseCase
import com.ericafenyo.bikediary.domain.settings.ObserveSettingsInteractor
import com.ericafenyo.bikediary.domain.settings.UpdateSettingsInteractor
import com.ericafenyo.bikediary.model.Settings
import com.ericafenyo.bikediary.model.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber


@HiltViewModel
internal class ProfileViewModel @Inject constructor(
  private val observeTheme: ObserveThemeInteractor,
  private val setTheme: SetThemeUseCase,
  private val settingsInteractor: ObserveSettingsInteractor,
  private val updateSettingsInteractor: UpdateSettingsInteractor,
) : ViewModel() {
  private val _isDarkTheme = MutableLiveData<Boolean>()
  val isDarkTheme: LiveData<Boolean> get() = _isDarkTheme

  private val _isLoading = MutableStateFlow(false)

  val state: Flow<ProfileViewState> = combine(
    _isLoading,
    settingsInteractor.asFlow().distinctUntilChanged()
  ) { isLoading, settings ->
    ProfileViewState(
      isLoading = isLoading,
      settings = settings
    )
  }

  init {
    settingsInteractor.invoke(Unit)

    viewModelScope.launch {
      delay(2000)
      _isLoading.value = true
    }
  }

  fun onEnableDarkThemeEvent(enabled: Boolean) {
    viewModelScope.launch {
      val theme = if (enabled) Theme.DARK else Theme.LIGHT
      setTheme(theme)
    }
  }

  fun updateSettings(settings: Settings) {
    Timber.d("Updating settings: $settings")
    viewModelScope.launch {
      updateSettingsInteractor(settings)
    }
  }
}

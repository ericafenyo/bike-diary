/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Eric Afenyo
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

package com.ericafenyo.bikediary.ui

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.data.settings.SetThemeUseCase
import com.ericafenyo.bikediary.model.Theme
import com.ericafenyo.bikediary.model.UserAuthenticationStatus
import com.ericafenyo.bikediary.ui.theme.ThemeDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val themeDelegate: ThemeDelegate,
  private val switchTheme: SetThemeUseCase,
  private val userAuthenticationStatus: UserAuthenticationStatus,
) : ViewModel(), ThemeDelegate by themeDelegate {

  private val _isLoggedIn = MutableStateFlow(false)
  val isLoggedIn = _isLoggedIn.asStateFlow()

  val currentUserImageUri = MutableLiveData<Uri>()

  fun isUserLoggedIn(): Flow<Boolean> = flow {
    emit(userAuthenticationStatus.isLoggedIn())
  }

  fun setTheme(theme: Theme) {
    viewModelScope.launch {
      switchTheme(theme)
    }
  }
}
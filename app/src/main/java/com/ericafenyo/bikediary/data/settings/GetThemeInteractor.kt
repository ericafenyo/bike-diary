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

package com.ericafenyo.bikediary.data.settings

import com.ericafenyo.bikediary.data.CoroutineInteractor
import com.ericafenyo.bikediary.di.qualifier.DefaultDispatcher
import com.ericafenyo.bikediary.model.Theme
import com.ericafenyo.bikediary.model.themeFromStorageKey
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetThemeInteractor @Inject constructor(
  @DefaultDispatcher dispatcher: CoroutineDispatcher,
  private val preferenceStorage: PreferenceStorage
) : CoroutineInteractor<Unit, Theme>(dispatcher) {

  override suspend fun execute(parameters: Unit): Theme {
    val selectedTheme = preferenceStorage.selectedTheme
    return themeFromStorageKey(selectedTheme) ?: Theme.LIGHT
  }
}
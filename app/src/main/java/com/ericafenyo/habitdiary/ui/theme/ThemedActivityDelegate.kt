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

package com.ericafenyo.habitdiary.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ericafenyo.habitdiary.data.settings.ObserveThemeInteractor
import com.ericafenyo.habitdiary.model.Theme
import com.ericafenyo.habitdiary.successOr
import kotlinx.coroutines.flow.collect

/**
 * Interface for delegating access to activity themes.
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2020-11-05
 */
interface ThemedActivityDelegate {
    /**
     * Allows observing of the current theme
     */
    val theme: LiveData<Theme>

    /**
     * Allows querying of the current theme synchronously
     */
    val currentTheme: Theme
}

class DefaultThemedActivityDelegate(
    observeThemeInteractor: ObserveThemeInteractor
) : ThemedActivityDelegate {
    override val theme: LiveData<Theme> = liveData {
        observeThemeInteractor.invoke(Unit)
            .collect { emit(it.successOr(Theme.LIGHT)) }
    }
    override val currentTheme: Theme
        get() = TODO("Not yet implemented")

}
/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 Eric Afenyo
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

package com.ericafenyo.habitdiary.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.data.data
import com.ericafenyo.data.domain.GetAdventuresUseCase
import com.ericafenyo.data.model.Adventure
import com.ericafenyo.data.succeeded
import com.ericafenyo.habitdiary.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
  private val getAdventures: GetAdventuresUseCase
) : ViewModel() {
  private val _state = MutableLiveData<UIState>()
  val state: LiveData<UIState> get() = _state

  private val _adventures = MutableLiveData<List<Adventure>>()
  val adventures: LiveData<List<Adventure>> get() = _adventures

  init {
    loadAdventures()
  }

  private fun loadAdventures() {
    viewModelScope.launch {
      _state.value = UIState(loading = true)
      getAdventures(Unit).run {
        if (succeeded) {
          _adventures.value = this.data!!
          if (this.data?.size!! < 1) {
            _state.value = UIState(empty = true)
          } else {
            _state.value = UIState(success = true)
          }
        } else {
          _state.value = UIState(error = true)
        }
      }
    }
  }
}

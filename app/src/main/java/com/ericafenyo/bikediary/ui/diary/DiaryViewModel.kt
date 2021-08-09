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

package com.ericafenyo.bikediary.ui.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.domain.observers.ObserveAdventures
import com.ericafenyo.bikediary.model.Adventure
import com.ericafenyo.bikediary.model.UIState
import com.ericafenyo.tracker.data.model.Result
import com.ericafenyo.tracker.data.model.data
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DiaryViewModel @Inject constructor(
  private val getAdventures: ObserveAdventures
) : ViewModel() {
  private val _state = MutableLiveData<UIState>()
  val state: LiveData<UIState> get() = _state

  private val _syncing = MutableLiveData<Boolean>()
  val syncing: LiveData<Boolean> get() = _syncing

  private val _adventures = MutableLiveData<List<Adventure>>()
  val adventures: LiveData<List<Adventure>> get() = _adventures

  init {
    loadAdventures()
  }

  private fun loadAdventures() = viewModelScope.launch {
    Timber.d("Loading adventures")
    // Start with a loading state
    _state.value = UIState(loading = true)

    kotlin.runCatching {
      getAdventures()
        .collect { result ->
          Timber.d("Got adventures");
          setAdventureResult(result)
        }
    }.onFailure {
      Timber.e(it, "Loading adventures")
    }.onSuccess {
      Timber.d("Loading adventures success $it")
    }
    /* .catch {
       Timber.d("An error occurred loading adventures")
       _adventures.value = emptyList();
       _state.value = UIState(error = true) }*/
//      .launchIn(viewModelScope)/
  }

  private fun setAdventureResult(result: Result<List<Adventure>>) {
    Timber.d("result result $result")
    val data = result.data // New variable created to benefit from smart cast.
    if (data != null) {
      if (data.isEmpty()) {
        _adventures.value = emptyList()
        _state.value = UIState(empty = true)
      } else {
        _adventures.value = data!!
        _state.value = UIState(success = true)
      }
    }
  }
}

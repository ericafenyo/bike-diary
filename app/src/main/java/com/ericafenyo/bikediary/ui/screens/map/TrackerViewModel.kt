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

package com.ericafenyo.bikediary.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.ui.Store
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.START
import com.ericafenyo.bikediary.ui.screens.map.TrackerAction.STOP
import com.ericafenyo.tracker.Tracker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TrackerViewModel @Inject constructor() : ViewModel(), Store<TrackerUiState, TrackerAction> {
  private val tracker: Tracker = Tracker.getInstance()

  override val state: StateFlow<TrackerUiState>
    get() = tracker.state.map { trackerState -> TrackerUiState.Loading }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TrackerUiState.Loading
      )

  override fun dispatch(action: TrackerAction) {
    when (action) {
      START -> viewModelScope.launch { tracker.start() }
      STOP -> viewModelScope.launch { tracker.stop() }
    }
  }
}

sealed class TrackerUiState {
  object Loading : TrackerUiState()
  data class Success(
    val trackerState: Tracker.State = Tracker.State.IDLE,
  )
}

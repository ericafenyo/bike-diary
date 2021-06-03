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

package com.ericafenyo.bikediary.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ericafenyo.tracker.datastore.CurrentState
import com.ericafenyo.tracker.datastore.PreferenceDataStore
import com.ericafenyo.tracker.datastore.RecordsProvider
import com.ericafenyo.tracker.location.SimpleLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@HiltViewModel
class MapViewModel @Inject constructor(
  private val dataStore: PreferenceDataStore,
  recordsProvider: RecordsProvider
) : ViewModel() {

  val locationUpdates: LiveData<SimpleLocation> = recordsProvider.singleRecord()
    .map { record -> record?.location }
    .asLiveData()

  val currentTrackerState: CurrentState = runBlocking { recordsProvider.getCurrentState().first() }

  private val _isTrackingOngoing = MutableLiveData<Boolean>()
  val isTrackingOngoing: LiveData<Boolean> get() = _isTrackingOngoing

  init {
    viewModelScope.launch {
      recordsProvider.getCurrentState().collect { currentState ->
        Timber.d("PreferenceDataStore $currentState")
        _isTrackingOngoing.value = currentState.isOngoing()
      }
    }
  }
}


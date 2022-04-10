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

package com.ericafenyo.bikediary.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.model.CaloriesQuest
import com.ericafenyo.bikediary.model.DistanceQuest
import com.ericafenyo.bikediary.model.Quests
import com.ericafenyo.bikediary.ui.Store
import com.ericafenyo.bikediary.ui.screens.dashboard.DashboardAction.LogTest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

@HiltViewModel
class DashboardViewModel @Inject constructor(
) : ViewModel(), Store<DashboardViewState, DashboardAction> {

  override val state: StateFlow<DashboardViewState> = combine(
    flowOf(false),
    flowOf(Quests(calories = CaloriesQuest(500), distance = DistanceQuest(1.1)))
  ) { isLoading, quests ->

    DashboardViewState(
      isLoading = isLoading,
      quests = quests
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.Eagerly,
    initialValue = DashboardViewState()
  )

  override fun dispatch(action: DashboardAction) {
    when (action) {
      is LogTest -> Timber.d("Logging action tests in view model")
    }
  }
}

data class DashboardViewState(
  val isLoading: Boolean = false,
  val quests: Quests = Quests(calories = CaloriesQuest(500), distance = DistanceQuest(1.1))
)

sealed class DashboardAction {
  data class LogTest(val message: String) : DashboardAction()
}

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

package com.ericafenyo.bikediary.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.ericafenyo.bikediary.domain.configuration.GetConfiguration
import com.ericafenyo.bikediary.model.Configuration
import com.ericafenyo.tracker.data.model.successOr
import com.ericafenyo.tracker.domain.bmi.BodyMassIndexParams
import com.ericafenyo.tracker.domain.bmi.CalibrateBodyMassIndexInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
  private val calibrateBodyMassIndex: CalibrateBodyMassIndexInteractor,
  private val getConfiguration: GetConfiguration
) : ViewModel() {

  private val _bodyMassIndex = MutableLiveData<Double>()
  val bodyMassIndex: LiveData<Double> = _bodyMassIndex

  init {
    loadBodyMassIndex()
  }

  private fun loadBodyMassIndex() {
    viewModelScope.launch {
      val configuration = getConfiguration().successOr(Configuration.default())
      val bmi = computeBodyMassIndex(configuration.height, configuration.weight)
      _bodyMassIndex.value = bmi
    }
  }

  private fun computeBodyMassIndex(height: Double, weight: Double): Double {
    val heightInMeters = height / 100
    return if (height <= 0) 0.0 else weight / (heightInMeters * heightInMeters)
  }


  fun saveBodyMassIndex(weight: Double, height: Double) = liveData {
    val params = BodyMassIndexParams(weight = weight, height = height)
    emit(calibrateBodyMassIndex(params))
  }
}

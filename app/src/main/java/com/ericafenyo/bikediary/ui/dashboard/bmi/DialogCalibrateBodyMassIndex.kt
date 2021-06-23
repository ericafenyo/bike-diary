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

package com.ericafenyo.bikediary.ui.dashboard.bmi

import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.DialogCalibrateBmiBinding
import com.ericafenyo.bikediary.util.Validator
import com.ericafenyo.bikediary.util.doOnTrue
import com.ericafenyo.bikediary.util.getOrEmpty
import com.ericafenyo.bikediary.util.round
import com.ericafenyo.bikediary.widget.DialogFragment
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DialogCalibrateBodyMassIndex : DialogFragment(R.layout.dialog_calibrate_bmi) {
  private val binding: DialogCalibrateBmiBinding by dataBinding()
  private val model: BodyMassIndexViewModel by viewModels()
  private val validator: Validator by lazy { Validator(requireContext(), true) }
  private var _block: ((DialogInterface, Double, Double) -> Unit)? = null

  override fun onCreated(view: View) = with(binding) {
    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = model

    binding.onAction = View.OnClickListener {
      val weight = editTextWeight.text.getOrEmpty()
      val height = editTextHeight.text.getOrEmpty()

      // Validate required weight
      val hasValidInputs = validator.isFieldNotEmpty(weight, textFieldWeight)
        .doOnTrue { validator.isFieldNotEmpty(height, textFieldHeight) }

      if (hasValidInputs) {
        Timber.d("weight: $weight height: $height")
        _block?.invoke(requireDialog(), weight.toDouble().round(1), height.toDouble().round(2))
      }
    }
  }

  fun onAction(block: (DialogInterface, Double, Double) -> Unit): DialogCalibrateBodyMassIndex {
    this._block = block
    return this
  }

  fun show(manager: FragmentManager) {
    super.show(manager, null)
  }
}

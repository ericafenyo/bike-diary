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

import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentHomeBinding
import com.ericafenyo.bikediary.ui.dashboard.bmi.DialogCalibrateBodyMassIndex
import com.ericafenyo.bikediary.util.AlertHelper
import com.ericafenyo.tracker.data.model.succeeded
import kotlinx.coroutines.CoroutineScope

class DashboardEventListener(
  fragment: Fragment,
  private val model: DashboardViewModel,
  private val binding: FragmentHomeBinding
) : OnDashboardEventListener {
  private val activity = fragment.requireActivity()
  private val navController = fragment.findNavController()
  private val scope: CoroutineScope = fragment.lifecycleScope
  private val owner: LifecycleOwner = fragment.viewLifecycleOwner
  private val alertHelper: AlertHelper by lazy { AlertHelper(activity) }

  override fun onEditBmi() {
    // launch dialog
    DialogCalibrateBodyMassIndex()
      .onAction { dialog, weight, height -> saveBodyMassIndex(dialog, weight, height) }
      .show(activity.supportFragmentManager)
  }

  private fun saveBodyMassIndex(dialog: DialogInterface, weight: Double, height: Double) {
    try {
      model.saveBodyMassIndex(weight, height).observe(owner, { result ->
        if (result.succeeded) {
          alertHelper.showToast("dddc")
        } else {
        }
      })
    } finally {
      dialog.dismiss()
    }
  }

  override fun launchHistory() {
    navController.navigate(R.id.action_dashboard_to_diary)
  }
}

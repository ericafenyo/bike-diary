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

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ericafenyo.habitdiary.R
import com.ericafenyo.habitdiary.databinding.FragmentDiaryBinding
import com.ericafenyo.habitdiary.util.SpaceItemDecoration
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.roundToInt


@AndroidEntryPoint
class DiaryFragment : Fragment(R.layout.fragment_diary) {
  private val binding: FragmentDiaryBinding by dataBinding()
  private val model: DiaryViewModel by viewModels()
  private val diaryAdapter = DiaryAdapter()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = model

    binding.diaryRecyclerView.apply {
      adapter = diaryAdapter
      setHasFixedSize(true)
      addItemDecoration(SpaceItemDecoration(requireContext().dp2px(6), requireContext().dp2px(12)))
    }
  }

  override fun onResume() {
    super.onResume()
    model.adventures.observe(viewLifecycleOwner, { trips ->
      diaryAdapter.submitList(trips)
      diaryAdapter.notifyDataSetChanged()
      Timber.d("Trippps $trips")
    })
  }

  override fun onDestroyView() {
    diaryAdapter.setListener(null)
    binding.diaryRecyclerView.adapter = null
    super.onDestroyView()
  }
}

fun Context.dp2px(dp: Int): Int {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics
  ).roundToInt()
}

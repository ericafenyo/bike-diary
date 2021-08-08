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

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ericafenyo.bikediary.Constants.ARGUMENT_ADVENTURE_ID
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentDiaryBinding
import com.ericafenyo.bikediary.ui.diary.DiaryAdapter.OnItemClickListener
import com.ericafenyo.bikediary.util.SpaceItemDecoration
import com.ericafenyo.bikediary.util.dp2px
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DiaryFragment : Fragment(R.layout.fragment_diary) {
  private val binding: FragmentDiaryBinding by dataBinding()
  private val model: DiaryViewModel by viewModels()
  private val diaryAdapter = DiaryAdapter()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    diaryAdapter.setListener(diaryItemClickListener)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = model

    binding.diaryRecyclerView.apply {
      adapter = diaryAdapter
      setHasFixedSize(true)
      addItemDecoration(SpaceItemDecoration(dp2px(6), dp2px(12)))
    }

    model.adventures.observe(viewLifecycleOwner, { trips ->
      Timber.d("adventures observe $trips")
      diaryAdapter.submitList(trips)
    })
  }

  private val diaryItemClickListener = object : OnItemClickListener {
    override fun onItemClick(view: View, adventureId: String) {

      val args = Bundle().apply { putString(ARGUMENT_ADVENTURE_ID, adventureId) }
      findNavController().navigate(R.id.action_diaryFragment_to_adventureDetailsFragment, args)
    }
  }

  override fun onDestroyView() {
    diaryAdapter.setListener(null)
    binding.diaryRecyclerView.adapter = null
    super.onDestroyView()
  }
}

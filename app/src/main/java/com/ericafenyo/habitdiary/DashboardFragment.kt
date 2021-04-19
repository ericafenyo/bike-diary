/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Eric Afenyo
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

package com.ericafenyo.habitdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ericafenyo.habitdiary.databinding.FragmentHomeBinding
import com.ericafenyo.habitdiary.model.Achievement
import com.ericafenyo.habitdiary.model.Badge
import com.ericafenyo.habitdiary.util.drawableFrom
import com.wada811.databinding.dataBinding
import model.getExplicitIntent

/**
 * A simple [Fragment] subclass for displaying users adventure metrics.
 */
class DashboardFragment : Fragment(R.layout.fragment_home) {
  private val binding: FragmentHomeBinding by dataBinding()


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.achievement = Achievement(
      totalDistance = 100.toDouble(),
      badge = Badge(
        distance = 53.toDouble(),
        icon = requireContext().drawableFrom(R.drawable.badge_level_one)
      )
    )

    view.findViewById<Button>(R.id.button_init).setOnClickListener {
      requireActivity().sendBroadcast(requireActivity().getExplicitIntent(R.string.tracker_action_initialize))
    }

    view.findViewById<Button>(R.id.button_start).setOnClickListener {
      requireActivity().sendBroadcast(requireActivity().getExplicitIntent(R.string.tracker_action_start))
    }

    view.findViewById<Button>(R.id.button_stop).setOnClickListener {
      requireActivity().sendBroadcast(requireActivity().getExplicitIntent(R.string.tracker_action_stop))
    }
  }
}
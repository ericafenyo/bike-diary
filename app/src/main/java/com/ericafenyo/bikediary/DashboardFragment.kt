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

package com.ericafenyo.bikediary

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ericafenyo.bikediary.databinding.FragmentHomeBinding
import com.ericafenyo.bikediary.model.Achievement
import com.ericafenyo.bikediary.model.Badge
import com.ericafenyo.bikediary.util.drawableFrom
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition.RIGHT_BOTTOM
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.wada811.databinding.dataBinding


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

    setWeightData(binding.chartWeight)
    setAdventureData(binding.chartAdventures)
  }

  private fun setWeightData(chart: LineChart) {
    chart.setPinchZoom(false)
    chart.axisRight.isEnabled = false
    chart.legend.isEnabled = false
    chart.isScaleYEnabled = false
    val description = chart.description
    description.text = "Weights"

    val xAxis = chart.xAxis
    xAxis.position = BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.granularity = 1f
    xAxis.labelCount = 7
    xAxis.valueFormatter = DayAxisValueFormatter(requireContext(), chart)

    val targetAxis = LimitLine(65f, "65.0kg")
    targetAxis.lineWidth = 1f
    targetAxis.lineColor = ContextCompat.getColor(requireContext(), R.color.color_green)
    targetAxis.enableDashedLine(20f, 20f, 0f)
    targetAxis.labelPosition = RIGHT_BOTTOM

    val yAxis = chart.axisLeft
    yAxis.setDrawLimitLinesBehindData(true);
    yAxis.addLimitLine(targetAxis)
    yAxis.setDrawAxisLine(false)


    val weights = listOf(56.8f, 60.7f, 70.4f, 50.4f, 68.5f, 64.3f, 60.0f, 56.3f)
    val values = weights.mapIndexed { index, weight -> Entry(index.toFloat(), weight, "Mon") }

    val dataset = LineDataSet(values, "")
    dataset.lineWidth = 2.5f
    dataset.color = ContextCompat.getColor(requireContext(), R.color.color_primary)
    dataset.setDrawCircles(false)
    dataset.setDrawValues(false)

    chart.data = LineData(dataset)
  }

  private fun setAdventureData(chart: BarChart) {
    chart.setPinchZoom(false)
    chart.axisRight.isEnabled = false
    chart.legend.isEnabled = false
    chart.isScaleYEnabled = false
    val description = chart.description
    description.text = "Weights"

    val xAxis = chart.xAxis
    xAxis.position = BOTTOM
    xAxis.setDrawGridLines(false)
    xAxis.granularity = 1f
    xAxis.labelCount = 7
    xAxis.valueFormatter = DayAxisValueFormatter(requireContext(), chart)

    val targetAxis = LimitLine(65f, "65.0kg")
    targetAxis.lineWidth = 1f
    targetAxis.lineColor = ContextCompat.getColor(requireContext(), R.color.color_green)
    targetAxis.enableDashedLine(20f, 20f, 0f)
    targetAxis.labelPosition = RIGHT_BOTTOM

    val yAxis = chart.axisLeft
    yAxis.setDrawLimitLinesBehindData(true);
    yAxis.addLimitLine(targetAxis)
    yAxis.setDrawAxisLine(false)


    val weights = listOf(56.8f, 60.7f, 70.4f, 50.4f, 68.5f, 64.3f, 60.0f, 56.3f)
    val values = weights.mapIndexed { index, weight -> BarEntry(index.toFloat(), weight) }

    val dataset = BarDataSet(values, "")
    //dataset.lineWidth = 2.5f
    dataset.color = ContextCompat.getColor(requireContext(), R.color.color_primary)
    //dataset.setDrawCircles(false)
    dataset.setDrawValues(false)

    chart.data = BarData(dataset)
  }
}

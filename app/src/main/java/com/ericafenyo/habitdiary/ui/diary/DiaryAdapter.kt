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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericafenyo.habitdiary.databinding.ItemDateBinding
import com.ericafenyo.habitdiary.databinding.ItemTripBinding
import com.ericafenyo.habitdiary.model.Trip

class DiaryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var _items: List<Any> = emptyList()
  private val items get() = _items

  fun submitList(newItems: List<Any>) {
    this._items = newItems
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      DATE_TYPE -> createDateViewHolder(parent)
      TRIP_TYPE -> createTripViewHolder(parent)
      else -> throw IllegalArgumentException("View type not supported $viewType")
    }
  }

  private fun createTripViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return TripViewHolder(binding)
  }

  private fun createDateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
    val binding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DateViewHolder(binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (getItemViewType(position)) {
      DATE_TYPE -> (holder as DateViewHolder).bind()
      TRIP_TYPE -> (holder as TripViewHolder).bind(items[position] as Trip)
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when {
      items[position] is Trip -> TRIP_TYPE
      items[position] is String -> DATE_TYPE
      else -> -1
    }
  }

  override fun getItemCount(): Int = items.size

  inner class TripViewHolder(
    private val binding: ItemTripBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(trip: Trip) {
      binding.trip = trip
    }
  }

  inner class DateViewHolder(
    private val binding: ItemDateBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
      val date = (items[adapterPosition] as String)
      binding.textDate.text = date
    }
  }

  companion object {
    private const val DATE_TYPE = 0
    private const val TRIP_TYPE = 1
  }
}

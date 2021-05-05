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

package com.ericafenyo.bikediary.util

import android.view.View
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.mancj.slimchart.SlimChart


@BindingAdapter("image")
fun ImageView.bindImage(url: String? = "") {
  Glide.with(context).load(url).into(this)
}

@BindingAdapter("showIf")
fun View.bindShowIf(show: Boolean) {
  visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("goneIf")
fun View.bindGoneIf(gone: Boolean) {
  visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("onTextChanged")
fun TextInputEditText.bindOnTextChanged(block: (view: View, text: String) -> Unit) {
  doOnTextChanged { text, _, _, _ -> block(this, text.toString()) }
}

@BindingAdapter("starts")
fun SlimChart.bindStarts(progress: Int = 0) {
  stats = floatArrayOf(progress.toFloat(), 100F)
}

@BindingAdapter("color")
fun SlimChart.bindColor(color: Int) {
  val track = ColorUtils.setAlphaComponent(color, 50)
  colors = intArrayOf(color, track)
}
/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Transway
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

package com.ericafenyo.bikediary.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.ericafenyo.bikediary.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator

class Button @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private var button: MaterialButton
  private var progress: CircularProgressIndicator

  private var _isLoading: Boolean = false
  private var _buttonText: String? = ""
  private var _buttonTextColor: Int = 0


  /**
   * This needs to be retained, supporting dataBinding with custom attributes
   *
   * @param isLoading set to true to show the progress indicator
   */
  fun setLoading(isLoading: Boolean) {
    _isLoading = isLoading
    setStates(context, _isLoading)
  }

  override fun setOnClickListener(listener: OnClickListener?) {
    button.setOnClickListener(listener)
  }

  init {
    // Inflate a custom view
    View.inflate(context, R.layout.view_loading_button, this).also { itemView ->
      button = itemView.findViewById(R.id.button)
      progress = itemView.findViewById(R.id.progress_circular)
    }

    // Bind xml attributes
    context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
      if (hasValue(R.styleable.LoadingButton_isLoading)) {
        _isLoading = getBoolean(R.styleable.LoadingButton_isLoading, false)
      }

      _buttonText = getString(R.styleable.LoadingButton_text)
      _buttonTextColor = getColor(R.styleable.LoadingButton_textColor, 0)
    }
  }

  private fun setStates(context: Context, loading: Boolean) {
    val transparentColor = ContextCompat.getColor(context, android.R.color.transparent)
    val color = if (loading) transparentColor else _buttonTextColor
    button.setTextColor(color)
    button.isEnabled = !loading
    button.text = _buttonText

    progress.isVisible = loading
  }
}

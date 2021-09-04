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

package com.ericafenyo.bikediary.ui.app

import android.view.View
import androidx.annotation.StringRes
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.DialogAlertBinding
import com.ericafenyo.bikediary.widget.DialogFragment
import com.wada811.databinding.dataBinding

class DialogAlert : DialogFragment(R.layout.dialog_alert) {
  private val binding: DialogAlertBinding by dataBinding()

  override fun onCreated(view: View) {}

  fun setTitle(@StringRes resId: Int): DialogAlert {
    binding.textTitle.setText(resId)
    return this
  }

  fun setMessage(@StringRes resId: Int): DialogAlert {
    binding.textMessage.setText(resId)
    return this
  }

  fun setAction(
    @StringRes resId: Int,
    block: (() -> Unit)? = null
  ): DialogAlert {
    binding.button.setText(resId)
    binding.button.setOnClickListener { if (block != null) block() else requireDialog().dismiss() }
    return this
  }
}
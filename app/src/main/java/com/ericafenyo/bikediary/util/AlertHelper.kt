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

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.R
import com.google.android.material.snackbar.Snackbar

class AlertHelper(context: Context) {
  private var toast: Toast? = null
  private var snack: Snackbar? = null
  private val appContext = context.applicationContext

  fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    if (toast != null) {
      toast = null
    }

    toast = Toast.makeText(appContext, message, duration)
    toast?.show()
  }

  fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    if (toast != null) {
      toast = null
    }

    toast = Toast.makeText(appContext, appContext.getString(resId), duration)
    toast?.show()
  }

  fun showPersistentSnack(
    view: View,
    @StringRes resId: Int,
    duration: Int = Snackbar.LENGTH_INDEFINITE
  ) {
    if (snack != null) {
      snack = null
    }

    snack = Snackbar.make(view, appContext.getString(resId), duration)
      .setAction(android.R.string.ok) { snack?.dismiss() }
    val snackView = snack?.view
    val snackTextView = snackView?.findViewById<View>(R.id.snackbar_text) as TextView

    snackTextView.maxLines = 3
    snack?.show()
  }

  fun showSnack(view: View, @StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    if (snack != null) {
      snack = null
    }

    snack = Snackbar.make(view, appContext.getString(resId), duration)
    snack?.show()
  }
}
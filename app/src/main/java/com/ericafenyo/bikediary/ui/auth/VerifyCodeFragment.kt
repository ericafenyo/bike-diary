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

package com.ericafenyo.bikediary.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentVerifyCodeBinding
import com.ericafenyo.bikediary.ui.auth.AuthenticationViewMode.AuthenticationAction
import com.ericafenyo.bikediary.util.EventObserver
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyCodeFragment : Fragment(R.layout.fragment_verify_code) {
  private val binding: FragmentVerifyCodeBinding by dataBinding()
  private val viewModel: AuthenticationViewMode by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = viewModel

    viewModel.events.observe(viewLifecycleOwner, EventObserver { action ->
      when (action) {
        AuthenticationAction.VERIFY_CODE -> onSubmit()
        AuthenticationAction.RESEND_CODE -> viewModel.resendCode()
      }
    })

    binding.apply {
      editTextCodeOne.addTextChangedListener(
        SingleValueTextWatcher(editTextCodeOne, editTextCodeTwo)
      )

      editTextCodeTwo.addTextChangedListener(
        SingleValueTextWatcher(editTextCodeTwo, editTextCodeThree)
      )

      editTextCodeThree.addTextChangedListener(
        SingleValueTextWatcher(editTextCodeThree, editTextCodeFour)
      )

      editTextCodeOne.setOnKeyListener(SingleValueKeyEvent(editTextCodeOne, null))
      editTextCodeTwo.setOnKeyListener(SingleValueKeyEvent(editTextCodeTwo, editTextCodeOne))
      editTextCodeThree.setOnKeyListener(SingleValueKeyEvent(editTextCodeThree, editTextCodeTwo))
      editTextCodeFour.setOnKeyListener(SingleValueKeyEvent(editTextCodeFour, editTextCodeThree))
    }
  }

  private fun onSubmit() {
    val codeOne = binding.editTextCodeOne.text?.toString() ?: ""
    val codeTwo = binding.editTextCodeTwo.text?.toString() ?: ""
    val codeThree = binding.editTextCodeThree.text?.toString() ?: ""
    val codeFour = binding.editTextCodeFour.text?.toString() ?: ""

    val codeString = codeOne + codeTwo + codeThree + codeFour

    val code: Int = runCatching { codeString.toInt() }.getOrDefault(0)

    viewModel.verifyCode(code)
  }
}

class SingleValueTextWatcher(
  private val currentView: View, private val nextView: View
) : TextWatcher {
  override fun beforeTextChanged(chars: CharSequence?, start: Int, count: Int, after: Int) {
    // "Not yet implemented"
  }

  override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {
    // "Not yet implemented"
  }

  override fun afterTextChanged(editable: Editable?) {
    // "Not yet implemented"
    val text = editable?.toString() ?: ""
    if (text.length == 1) {
      nextView.requestFocus()
    }
  }
}

class SingleValueKeyEvent internal constructor(
  private val currentView: EditText,
  private val previousView: EditText?
) : View.OnKeyListener {
  override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
    if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.edit_text_code_one && currentView.text.isEmpty()) {
      //If current is empty then previous EditText's number will also be deleted
      previousView!!.text = null
      previousView.requestFocus()
      return true
    }
    return false
  }
}

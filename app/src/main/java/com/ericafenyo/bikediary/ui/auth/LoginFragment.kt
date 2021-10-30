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
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentLoginBinding
import com.ericafenyo.bikediary.util.EventObserver
import com.ericafenyo.bikediary.util.Validator
import com.ericafenyo.bikediary.util.doOnTrue
import com.ericafenyo.bikediary.widget.dialog.Alert
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
  private val binding: FragmentLoginBinding by dataBinding()
  private val loginViewModel: LoginViewModel by viewModels()
  private val inputValidator by lazy { Validator(requireContext()) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = loginViewModel

    loginViewModel.message.observe(viewLifecycleOwner, { message ->
      if (message != null) {
        Alert.Builder(requireContext())
          .from(message)
          .build()
          .show(this)
      }
    })

    loginViewModel.state.observe(viewLifecycleOwner, { state ->
      if (state != null && state.success) {
        requireActivity().onBackPressed()
      }
    })

    // Clear input errors when the user is typing
    clearInputErrorsOnChange()

    // Listen and handle sign in button press
    loginViewModel.launchLoginAction.observe(viewLifecycleOwner, EventObserver {
      onSubmit()
    })

    // Listen and handle sign up button press. Note:
    // This just launches the signup page.
    loginViewModel.launchRegisterAction.observe(viewLifecycleOwner, EventObserver {
      findNavController().navigate((R.id.action_login_to_signup))
    })
  }

  private fun clearInputErrorsOnChange() {
    binding.editTextEmail.doAfterTextChanged {
      binding.textFieldEmail.error = null
    }

    binding.editTextPassword.doAfterTextChanged {
      binding.textFieldPassword.error = null
    }
  }

  private fun onSubmit() = with(binding) {
    // Extract the texts from the inputs.
    val email = editTextEmail.text?.toString() ?: ""
    val password = editTextPassword.text?.toString() ?: ""

    // Validate inputs and notify users about the errors.
    val hasValidInputs = validateInputs(email, password)

    // Submit the form only if all inputs are valid.
    if (hasValidInputs) {
      submit(email, password)
    }
  }

  private fun submit(email: String, password: String) {
    loginViewModel.authenticate(email, password)
  }

  private fun validateInputs(email: String, password: String): Boolean = with(binding) {
    // Validate required email
    inputValidator.isFieldNotEmpty(email, textFieldEmail)
      // Validate valid email
      .doOnTrue { inputValidator.isEmailValid(email, textFieldEmail) }
      // Validate required password
      .doOnTrue { inputValidator.isFieldNotEmpty(password, textFieldPassword) }
  }
}

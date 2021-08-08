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
import com.ericafenyo.bikediary.databinding.FragmentRegisterBinding
import com.ericafenyo.bikediary.util.EventObserver
import com.ericafenyo.bikediary.util.Validator
import com.ericafenyo.bikediary.util.doOnTrue
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_register) {
  private val binding: FragmentRegisterBinding by dataBinding()
  private val registerModel: RegisterViewModel by viewModels()
  private val inputValidator by lazy { Validator(requireContext()) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = registerModel

    // Clear input errors when the user is typing
    clearInputErrorsOnChange()

    // Listen and handle sign up button press.
    registerModel.registerAction.observe(viewLifecycleOwner, EventObserver {
      onSubmit()
    })

    // This launches the signup page.
    registerModel.launchLoginAction.observe(viewLifecycleOwner, EventObserver {
      findNavController().navigate((R.id.action_signup_to_login))
    })
  }

  private fun clearInputErrorsOnChange() {
    binding.editTextName.doAfterTextChanged {
      binding.inputFieldEmail.error = null
    }

    binding.editTextEmail.doAfterTextChanged {
      binding.inputFieldEmail.error = null
    }

    binding.editTextPassword.doAfterTextChanged {
      binding.inputFieldPassword.error = null
    }

    binding.editTextConfirmPassword.doAfterTextChanged {
      binding.inputFieldConfirmPassword.error = null
    }
  }

  private fun onSubmit() = with(binding) {
    // Extract the texts from the inputs.
    val name = editTextName.text?.toString() ?: ""
    val email = editTextEmail.text?.toString() ?: ""
    val password = editTextPassword.text?.toString() ?: ""
    val confirmedPassword = editTextConfirmPassword.text?.toString() ?: ""

    // Validate inputs and notify users about the errors.
    val hasValidInputs = validateInputs(name, email, password, confirmedPassword)

    // Submit the form only if all inputs are valid.
    if (hasValidInputs) {
      submit()
    }
  }

  private fun submit() {
    Timber.d("Submitting form")
  }

  private fun validateInputs(
    name: String,
    email: String,
    password: String,
    confirmedPassword: String
  ): Boolean = with(binding) {
    // Validate required name
    return inputValidator.isFieldNotEmpty(name, inputFieldName)
      // Validate required email
      .doOnTrue { inputValidator.isFieldNotEmpty(email, inputFieldEmail) }
      // Validate valid email
      .doOnTrue { inputValidator.isEmailValid(email, inputFieldEmail) }
      // Validate required password
      .doOnTrue { inputValidator.isFieldNotEmpty(password, inputFieldPassword) }
      // Validate valid password
      .doOnTrue { inputValidator.isPasswordValid(password, inputFieldPassword) }
      // Validate required confirmed password
      .doOnTrue { inputValidator.isFieldNotEmpty(confirmedPassword, inputFieldConfirmPassword) }
      // Validate matched password
      .doOnTrue {
        inputValidator.isPasswordMatched(
          password, confirmedPassword, binding.inputFieldConfirmPassword
        )
      }
  }
}

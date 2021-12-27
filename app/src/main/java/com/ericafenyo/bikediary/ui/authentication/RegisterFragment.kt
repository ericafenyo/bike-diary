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

package com.ericafenyo.bikediary.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentRegisterBinding
import com.ericafenyo.bikediary.ui.authentication.RegisterViewModel.Action.CREATE_ACCOUNT
import com.ericafenyo.bikediary.ui.authentication.RegisterViewModel.Action.LAUNCH_LOGIN
import com.ericafenyo.bikediary.util.EventObserver
import com.ericafenyo.bikediary.util.Validator
import com.ericafenyo.bikediary.util.doOnTrue
import com.ericafenyo.bikediary.widget.dialog.Alert
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
  private val binding: FragmentRegisterBinding by dataBinding()
  private val registerViewModel: RegisterViewModel by viewModels()
  private val inputValidator by lazy { Validator(requireContext()) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = registerViewModel

    // Clear input errors when the user is typing
    clearInputErrorsOnChange()

    registerViewModel.message.observe(viewLifecycleOwner, { message ->
      if (message != null) {
        Alert.Builder(requireContext())
          .from(message)
          .build()
      }
    })

    // Listen and handle sign up button press.
    registerViewModel.events.observe(viewLifecycleOwner, EventObserver { action ->
      when (action) {
        CREATE_ACCOUNT -> onSubmit()
        LAUNCH_LOGIN -> TODO()
      }
    })

    binding.apply {
      editTextFirstName.setText("Eric")
      editTextLastName.setText("Afenyo")
      editTextEmail.setText("eric.afenyo@transway.fr")
      editTextPassword.setText("Passw0rd")
      editTextConfirmPassword.setText("Passw0rd")
    }
  }

  private fun clearInputErrorsOnChange() {
    binding.editTextFirstName.doAfterTextChanged {
      binding.textFieldFirstName.error = null
    }

    binding.editTextLastName.doAfterTextChanged {
      binding.textFieldLastName.error = null
    }

    binding.editTextEmail.doAfterTextChanged {
      binding.textFieldEmail.error = null
    }

    binding.editTextPassword.doAfterTextChanged {
      binding.textFieldPassword.error = null
    }

    binding.editTextConfirmPassword.doAfterTextChanged {
      binding.textFieldConfirmPassword.error = null
    }
  }

  private fun onSubmit() = with(binding) {
    // Extract the texts from the inputs.
    val firstName = editTextFirstName.text?.toString() ?: ""
    val lastName = editTextLastName.text?.toString() ?: ""
    val email = editTextEmail.text?.toString() ?: ""
    val password = editTextPassword.text?.toString() ?: ""
    val confirmedPassword = editTextConfirmPassword.text?.toString() ?: ""

    // Validate inputs and notify users about the errors.
    val hasValidInputs = validateInputs(firstName, lastName, email, password, confirmedPassword)

    // Submit the form only if all inputs are valid.
    if (hasValidInputs) {
      submit(firstName, lastName, email, password)
    }
  }

  private fun submit(
    firstName: String,
    lastName: String,
    email: String,
    password: String
  ) {
    registerViewModel.createAccount(firstName, lastName, email, password)
  }

  private fun validateInputs(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    confirmedPassword: String
  ): Boolean = with(binding) {
    // Validate required first name
    return inputValidator.isFieldNotEmpty(firstName, textFieldFirstName)
      // Validate required first name
      .doOnTrue { inputValidator.isFieldNotEmpty(lastName, textFieldLastName) }
      // Validate required email
      .doOnTrue { inputValidator.isFieldNotEmpty(email, textFieldEmail) }
      // Validate valid email
      .doOnTrue { inputValidator.isEmailValid(email, textFieldEmail) }
      // Validate required password
      .doOnTrue { inputValidator.isFieldNotEmpty(password, textFieldPassword) }
      // Validate valid password
      .doOnTrue { inputValidator.isPasswordValid(password, textFieldPassword) }
      // Validate required confirmed password
      .doOnTrue { inputValidator.isFieldNotEmpty(confirmedPassword, textFieldConfirmPassword) }
      // Validate matched password
      .doOnTrue {
        inputValidator.isPasswordMatched(
          password, confirmedPassword, binding.textFieldConfirmPassword
        )
      }
  }
}

/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 Transway
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
import com.ericafenyo.bikediary.R
import com.google.android.material.textfield.TextInputLayout
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

/**
 * Contains methods for validation input fields.
 *
 * @property context
 *
 * @author Eric
 * @since 1.0
 *
 * created on 2021-05-14
 */
class Validator constructor(
  private val context: Context,
  private val hideErrorDrawable: Boolean = false,
) {

  fun isEmailValid(value: String, inputField: TextInputLayout): Boolean {
    val emailText = value.trim()
    if (EMAIL_REGEX.matches(emailText)) {
      hideRequiredError(inputField)
      return true
    } else {
      showInvalidEmailError(inputField)
      return false
    }
  }

  fun isPasswordValid(value: String, inputField: TextInputLayout): Boolean {
    val passwordText = value.trim()
    if (PASSWORD_REGEX.matches(passwordText)) {
      hideRequiredError(inputField)
      return true
    } else {
      showInvalidPasswordError(inputField)
      return false
    }
  }

  fun isFieldNotEmpty(value: String, inputField: TextInputLayout): Boolean {
    Timber.d("Field empty: $value")
    val inputText = value.trim()
    if (inputText.isNotEmpty()) {
      hideRequiredError(inputField)
      return true
    } else {
      showRequiredError(inputField)
      return false
    }
  }

  fun isPasswordMatched(
    password: String,
    confirmedPassword: String,
    inputField: TextInputLayout
  ): Boolean {
    val passwordText = password.trim()
    val confirmedPasswordText = confirmedPassword.trim()

    if (passwordText == confirmedPasswordText) {
      hideRequiredError(inputField)
      return true
    } else {
      inputField.error = context.getString(R.string.input_error_password_not_matched)
      if (hideErrorDrawable) {
        inputField.errorIconDrawable = null
      }
      return false
    }
  }

  fun isValidDate(
    date: String,
    inputField: TextInputLayout,
    isOptional: Boolean,
  ): Boolean {
    val dateText = date.trim()

    if (isOptional) {
      // Is optional, check if its empty then return valid
      if (dateText.isEmpty()) {
        return true
      } else {
        // Is optional but its not empty. Validate date.
        return validateLocalDate(dateText, inputField)
      }
    } else {
      // Is not optional. Validate date.
      return validateLocalDate(dateText, inputField)
    }
  }

  private fun hideRequiredError(inputField: TextInputLayout) {
    inputField.error = null
  }

  private fun showRequiredError(inputField: TextInputLayout) {
    inputField.error = context.getString(R.string.input_error_field_required)
    if (hideErrorDrawable) {
      inputField.errorIconDrawable = null
    }
  }

  private fun showInvalidPasswordError(inputField: TextInputLayout) {
    inputField.error = context.getString(R.string.input_helper_text_password_constraints)
  }

  private fun showInvalidEmailError(inputField: TextInputLayout) {
    inputField.error = context.getString(R.string.input_error_invalid_email)
    if (hideErrorDrawable) {
      inputField.errorIconDrawable = null
    }
  }

  private fun validateLocalDate(date: String, inputField: TextInputLayout): Boolean {
    try {
      LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
      inputField.error = null
      return true
    } catch (_: Exception) {
      inputField.error = context.getString(R.string.input_error_invalid_date)
      return false
    }
  }

  companion object {
    @JvmStatic
    val EMAIL_REGEX = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")

    @JvmStatic
    val PASSWORD_REGEX = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
  }
}

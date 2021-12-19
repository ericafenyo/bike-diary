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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.databinding.FragmentAuthenticationBinding
import com.ericafenyo.bikediary.ui.authentication.AuthenticationViewModel.AuthenticationAction.LAUNCH_LOGIN_PAGE
import com.ericafenyo.bikediary.ui.authentication.AuthenticationViewModel.AuthenticationAction.LAUNCH_REGISTER_PAGE
import com.ericafenyo.bikediary.util.EventObserver
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
  private val binding: FragmentAuthenticationBinding by dataBinding()
  private val viewModel: AuthenticationViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.lifecycleOwner = viewLifecycleOwner
    binding.model = viewModel

    viewModel.events.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        LAUNCH_LOGIN_PAGE -> launchLoginPage()
        LAUNCH_REGISTER_PAGE -> launchRegisterPage()
      }
    })
  }

  private fun launchRegisterPage() {
    findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationToRegister())
  }

  private fun launchLoginPage() {
    findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationToLogin())
  }
}
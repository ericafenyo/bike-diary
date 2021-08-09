/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2020 Eric Afenyo
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

package com.ericafenyo.bikediary.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.data.trip.TripRepository
import com.ericafenyo.bikediary.databinding.ActivityMainBinding
import com.ericafenyo.bikediary.model.Theme
import com.ericafenyo.bikediary.network.adventure.BikeDiaryService
import com.ericafenyo.tracker.datastore.RecordsProvider
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private lateinit var navController: NavController

  private val binding: ActivityMainBinding by dataBinding()
  private val viewModel: MainActivityViewModel by viewModels()

  @Inject lateinit var tripRepository: TripRepository
  @Inject lateinit var provider: RecordsProvider
  @Inject lateinit var service: com.ericafenyo.bikediary.network.adventure.BikeDiaryService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Update theme
    updateForTheme(viewModel.currentTheme)

    setContentView(R.layout.activity_main)

    viewModel.theme.observe(this, Observer(::updateForTheme))

    navController = getNavController()

    binding.bottomNavigation.setupWithNavController(navController)
  }

  private fun getNavController(): NavController {
    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
    return navHostFragment.navController
  }
}

fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
  Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
  Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
}
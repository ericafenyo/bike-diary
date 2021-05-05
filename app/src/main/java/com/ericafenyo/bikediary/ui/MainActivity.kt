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
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.data.trip.TripRepository
import com.ericafenyo.bikediary.databinding.ActivityMainBinding
import com.ericafenyo.bikediary.model.Theme
import com.ericafenyo.bikediary.util.setupWithNavController
import com.ericafenyo.data.api.BikeDiaryService
import com.ericafenyo.data.repository.AdventureRepository
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private val binding: ActivityMainBinding by dataBinding()
  private val viewModel: MainActivityViewModel by viewModels()

  private var currentNavController: LiveData<NavController>? = null

  @Inject lateinit var adventureRepository: AdventureRepository

  @Inject lateinit var tripRepository: TripRepository
  @Inject lateinit var service: BikeDiaryService

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Update theme
    updateForTheme(viewModel.currentTheme)

    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      setupBottomNavigationBar()
    } // Else, need to wait for onRestoreInstanceState

    viewModel.theme.observe(this, Observer(::updateForTheme))

    // sendBroadcast(ExplicitIntent(this, string.tracker_action_end_analysis))
    // EditAdventureActivity.getStartIntent(this).also { startActivity(it) }

    GlobalScope.launch {
//      Timber.d("Drafting data")
//      adventureRepository.draftAdventures()
      //val gj = "{\"type\":\"FeatureCollection\",\"features\":[{\"geometry\":{\"coordinates\":[24.93834,60.16983],\"type\":\"Point\"},\"id\":\"af8e6f087eaf44089cdeb56b1242a87c\",\"properties\":{},\"type\":\"Feature\"}]}"
      //val fileName = service.getStaticMap(gj)
      //Timber.d("fileNamefileName $fileName")
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    // Now that BottomNavigationBar has restored its instance state
    // and its selectedItemId, we can proceed with setting up the
    // BottomNavigationBar with Navigation
    setupBottomNavigationBar()
  }

  private fun setupBottomNavigationBar() {
    val bottomNavigationView = binding.bottomNavigation
    val navGraphIds = listOf(
      R.navigation.dashboard,
      R.navigation.profile,
      R.navigation.diary,
      R.navigation.map
    )

    // Setup the bottom navigation view with a list of navigation graphs
    val controller = bottomNavigationView.setupWithNavController(
      navGraphIds = navGraphIds,
      fragmentManager = supportFragmentManager,
      containerId = R.id.nav_host_container,
      intent = intent
    )
    currentNavController = controller
  }

  override fun onSupportNavigateUp(): Boolean {
    return currentNavController?.value?.navigateUp() ?: false
  }
}

fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
  Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
  Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
}

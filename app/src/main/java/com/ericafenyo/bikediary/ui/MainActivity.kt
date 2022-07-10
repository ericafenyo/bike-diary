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
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.ericafenyo.bikediary.ui.screens.MainContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
//  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

//    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent { MainContent() }
  }
}


//
//    lifecycleScope.launch {
//      repeatOnLifecycle(Lifecycle.State.STARTED) {
//        viewModel.isUserLoggedIn().collect { isLoggedIn ->
//          Timber.d("User logged in: $isLoggedIn")
//          if (!isLoggedIn) {
//            // The user is not logged in, Launch the login page
////            startActivity(AuthenticationActivity.getStartIntent(this@MainActivity))
//          }
//        }
//      }
//    }

//    // Update theme
//    updateForTheme(Theme.LIGHT)
//
//    setContentView(R.layout.activity_main)
//
////    viewModel.theme.observe(this, Observer(::updateForTheme))
//
//    findViewById<BottomNavigationView>(R.id.bottom_navigation).setupWithNavController(navController)
//  }

//  private val navController: NavController
//    get() = (supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment).navController
//
//  companion object {
//    /**
//     * Creates an intent for starting this activity
//     * @param packageContext the context we are navigating from
//     *
//     * @return an [Intent]
//     */
//    fun getStartIntent(packageContext: Context): Intent {
//      return Intent(packageContext, MainActivity::class.java)
//    }
//  }
//}

//
//fun AppCompatActivity.updateForTheme(theme: Theme) = when (theme) {
//  Theme.DARK -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
//  Theme.LIGHT -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
//}

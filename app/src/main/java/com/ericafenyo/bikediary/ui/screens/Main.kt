/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2022 Eric Afenyo
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

package com.ericafenyo.bikediary.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ericafenyo.bikediary.libs.icons.Icons
import com.ericafenyo.bikediary.ui.authentication.AuthenticationActivity
import com.ericafenyo.bikediary.ui.navigation.BottomNavigation
import com.ericafenyo.bikediary.ui.navigation.BottomNavigationBar
import com.ericafenyo.bikediary.ui.navigation.NavigationHost
import com.ericafenyo.bikediary.ui.screens.map.TrackingActivity

@Composable
fun MainContent() {
  val navController = rememberNavController()
  val bottomNavigation = remember(navController) {
    BottomNavigation(navController)
  }
  val context = LocalContext.current

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination
  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { context.startActivity(TrackingActivity.getStartIntent(context)) },
        shape = CircleShape,
      ) {
        Icon(painter = Icons.Add, contentDescription = null)
      }
    },
    floatingActionButtonPosition = FabPosition.Center,
    bottomBar = {
      BottomNavigationBar(currentDestination = currentDestination, onItemClicked = { destination ->
        bottomNavigation.navigateTo(destination)
      })
    }
  ) { padding ->

    /*
     * This is where all the screens  are inflated.
     */
    NavigationHost(
      navController = navController,
      modifier = Modifier.padding(padding)
    )
  }
}

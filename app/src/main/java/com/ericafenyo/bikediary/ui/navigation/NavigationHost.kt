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

package com.ericafenyo.bikediary.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ericafenyo.bikediary.R
import com.ericafenyo.bikediary.ui.screens.adventures.AdventuresNavigation
import com.ericafenyo.bikediary.ui.screens.adventures.adventuresGraph
import com.ericafenyo.bikediary.ui.screens.dashboard.DashboardNavigation
import com.ericafenyo.bikediary.ui.screens.dashboard.dashboardGraph
import com.ericafenyo.bikediary.ui.screens.profile.ProfileNavigation
import com.ericafenyo.bikediary.ui.screens.profile.profileGraph

@Composable
fun NavigationHost(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = DashboardNavigation.ROUTE
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {
    dashboardGraph()
    adventuresGraph()
    profileGraph()
  }
}

val bottomNavigationItems = listOf(
  NavigationDestination(
    route = DashboardNavigation.ROUTE,
    activeIcon = R.drawable.ic_chart_pie,
    inactiveIcon = R.drawable.ic_chart_pie,
    label = R.string.title_dashboard
  ),

  NavigationDestination(
    route = AdventuresNavigation.ROUTE,
    activeIcon = R.drawable.ic_feed,
    inactiveIcon = R.drawable.ic_feed,
    label = R.string.title_diary
  ),

  NavigationDestination(
    route = ProfileNavigation.ROUTE,
    activeIcon = R.drawable.ic_user,
    inactiveIcon = R.drawable.ic_user,
    label = R.string.title_profile
  )
)

data class NavigationDestination(
  val route: String,
  val activeIcon: Int,
  val inactiveIcon: Int,
  val label: Int
)

class BottomNavigation(private val navController: NavHostController) {
  fun navigateTo(destination: NavigationDestination) {
    navController.navigate(destination.route) {

      popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
      }

      launchSingleTop = true

      restoreState = true
    }
  }
}

@Composable
fun BottomNavigationBar(
  modifier: Modifier = Modifier,
  currentDestination: NavDestination?,
  onItemClicked: (NavigationDestination) -> Unit,
) {
  BottomNavigation(
    modifier = modifier,
    backgroundColor = MaterialTheme.colors.background,
  ) {

    bottomNavigationItems.forEach { item ->
      val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

      BottomNavigationItem(
        selected = false,
        icon = {
          Icon(
            painter = if (selected) {
              painterResource(id = item.activeIcon)
            } else {
              painterResource(id = item.inactiveIcon)
            },
            contentDescription = null
          )
        },
        onClick = { onItemClicked(item) }
      )
    }
  }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
  BottomNavigationBar(onItemClicked = {}, currentDestination = null)
}

package com.pr.menusandnav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog


@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.AddAcoount.route,
        modifier = modifier.padding(paddingValues)
    ) {
        composable(Screen.DrawerScreen.AddAcoount.route) {

        }

        composable(Screen.DrawerScreen.Account.route) {

        }

        dialog(Screen.DrawerScreen.Subscription.route) {

        }
    }
}

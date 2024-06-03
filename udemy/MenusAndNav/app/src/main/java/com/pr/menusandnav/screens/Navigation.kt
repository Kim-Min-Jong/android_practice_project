package com.pr.menusandnav.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pr.menusandnav.MainViewModel


@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.DrawerScreen.Account.route,
        modifier = modifier.padding(paddingValues)
    ) {

        composable(Screen.DrawerScreen.Account.route) {
            AccountScreen()
        }

        composable(Screen.DrawerScreen.Subscription.route) {

        }
    }
}

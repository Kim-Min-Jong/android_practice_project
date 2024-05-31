package com.pr.wishlist.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pr.wishlist.Screen
import com.pr.wishlist.WishViewModel

@Composable
fun Navigation(
    viewModel: WishViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeView(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(
            Screen.AddScreen.route + "/{id}",
            // 경로에 id라는 매개변수를 받음 (타입 지정 가능)
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
                defaultValue = 0L
                nullable = false
            })
        ) { entry ->
            // 매개변수로 받은 id를 찾음
            val id = if (entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            AddEditDetailView(
                id = id,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

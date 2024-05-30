package com.pr.wishlist

// navigation을 위한 sealed class
sealed class Screen(
    val route: String
) {
    object HomeScreen: Screen("home_screen")
    object AddScreen: Screen("add_screen")
}

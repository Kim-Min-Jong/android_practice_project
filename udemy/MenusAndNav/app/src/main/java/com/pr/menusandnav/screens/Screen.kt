package com.pr.menusandnav.screens

import androidx.annotation.DrawableRes
import com.pr.menusandnav.R

// Drawer Click을 통해 이동할 화면들을 미리 정의
sealed class Screen(
    val title: String,
    val route: String
) {

    sealed class BottomScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val icon: Int
    ): Screen(bTitle, bRoute) {
        object Home: BottomScreen(
            "Home",
            "home",
            R.drawable.ic_music_video
        )
        object Library: BottomScreen(
            "Library",
            "library",
            R.drawable.ic_music_library
        )
        object Browse: BottomScreen(
            "Home",
            "home",
            R.drawable.ic_browse
        )

    }

    sealed class DrawerScreen(
        val dTitle: String,
        val dRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(dTitle, dRoute) {
        object Account : DrawerScreen(
            "Account",
            "account",
            R.drawable.ic_account
        )

        object Subscription : DrawerScreen(
            "Subscription",
            "subscribe",
            R.drawable.ic_subscribe
        )

        object AddAcoount : DrawerScreen(
            "Add Account",
            "add_account",
            R.drawable.ic_add_account
        )

    }
}

val screensInDrawer = listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.Subscription,
    Screen.DrawerScreen.AddAcoount,
)

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Library,
    Screen.BottomScreen.Browse
)

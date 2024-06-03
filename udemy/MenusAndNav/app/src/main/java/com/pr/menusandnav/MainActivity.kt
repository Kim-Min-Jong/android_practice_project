package com.pr.menusandnav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pr.menusandnav.screens.MainScreen
import com.pr.menusandnav.ui.theme.MenusAndNavTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenusAndNavTheme {
                MainScreen()
            }
        }
    }
}

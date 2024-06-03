package com.pr.menusandnav

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pr.menusandnav.screens.Screen

class MainViewModel: ViewModel() {

    // viewModel에서 navigation 처리를 위한 현재 화면 변수
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.DrawerScreen.AddAcoount)
    val currentScreen: State<Screen> = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }
}

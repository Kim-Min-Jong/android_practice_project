package com.pr.recipe_app.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.pr.recipe_app.MainViewModel
import com.pr.recipe_app.data.Category

@Composable
fun RecipeApp(
    navController: NavHostController
) {
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.categoryState

    NavHost(navController = navController, startDestination = Screen.RecipeScreen.route) {
        composable(route = Screen.RecipeScreen.route) {
            RecipeScreen(viewState = viewState, navigateToDetail = {
                // 탐색 컴포저블 간 데이터 교환을 위해 statehandle에 데이터를 보관하고 다음 화면으로 넘어가 찾음
                // currentBackStackEntry - 탐색 화면에 관한 데이터들을 갖고있는 entry
                navController.currentBackStackEntry?.savedStateHandle?.set("cat", it)
                // 탐색 시작
                navController.navigate(Screen.DetailScreen.route)
            })
        }

        composable(route = Screen.DetailScreen.route) {
            // 이전 엔트리 정보(previousBackStackEntry) 를 통해 데이터를 가져옴
            val category =
                navController.previousBackStackEntry?.savedStateHandle?.get<Category>("cat")
                    ?: Category("", "", "", "")
            CategoryDetailScreen(category = category)
        }
    }
}

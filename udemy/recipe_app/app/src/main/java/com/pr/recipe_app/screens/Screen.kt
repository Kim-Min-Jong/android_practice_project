package com.pr.recipe_app.screens

// 탐색 종착지 클래스
// sealed class를 통한 종류 제한
sealed class Screen(val route: String) {
    object RecipeScreen: Screen("recipescreen")
    object DetailScreen: Screen("detailscreen")
}

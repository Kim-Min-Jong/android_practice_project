package com.pr.recipe_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RecipeApp(
    modifier: Modifier = Modifier
) {
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.categoryState

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            // 로딩 상태라면
            viewState.loading -> {
                // 프로그레스 생성
                CircularProgressIndicator(
                    modifier = modifier.align(Alignment.Center)
                )
            }

            // 에러가 있다면
            viewState.error != null -> {
                Text(text = "ERROR OCURRED")
            }

            // 이외 로딩 성공
            else -> {

            }
        }
    }
}

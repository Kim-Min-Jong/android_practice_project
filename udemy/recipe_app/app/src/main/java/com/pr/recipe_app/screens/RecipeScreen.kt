package com.pr.recipe_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.pr.recipe_app.MainViewModel
import com.pr.recipe_app.data.Category

@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    viewState: MainViewModel.RecipeState,
    navigateToDetail: (Category) -> Unit
) {
    val recipeViewModel: MainViewModel = viewModel()

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
                CategoryScreen(categories = viewState.list) {
                    navigateToDetail
                }
            }
        }
    }
}

// 카테고리 리사이클러 뷰 (리스트)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    navigateToDetail: (Category) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
    ) {
        items(categories) {
            CategoryItem(category = it) {
                navigateToDetail
            }
        }
    }
}

// 리사이클러 뷰 아이템
@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    // 탐색 할 때 호출 될 람다
    navigateToDetail: (Category) -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .clickable {
                navigateToDetail(category)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // image with coil
        Image(
            // 비동기적으로 이미지를 가져옴
            painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
            contentDescription = "",
            modifier = modifier
                .fillMaxSize()
                // 1:1 비율로 맞춤
                .aspectRatio(1f)
        )

        // text
        Text(
            text = category.strCategory,
            color = Color.Black,
            style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = modifier.padding(top = 4.dp)
        )
    }
}

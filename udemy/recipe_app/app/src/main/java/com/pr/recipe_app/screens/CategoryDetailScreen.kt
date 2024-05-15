package com.pr.recipe_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.pr.recipe_app.data.Category

@Composable
fun CategoryDetailScreen(
    category: Category
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = category.strCategory, textAlign = TextAlign.Center)
        // image with coil
        Image(
            // 비동기적으로 이미지를 가져옴
            painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
            contentDescription = "${category.strCategory} Thumbnail",
            modifier = Modifier
                .wrapContentSize()
                // 1:1 비율로 맞춤
                .aspectRatio(1f)
        )
        // 긴 텍스트를 정렬 -> Justify
        Text(
            text = category.strCategoryDescription,
            textAlign = TextAlign.Justify,
            // text 영역만 스크롤 가능하도록
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
    }
}

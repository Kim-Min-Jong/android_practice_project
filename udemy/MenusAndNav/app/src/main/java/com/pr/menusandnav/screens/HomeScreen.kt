package com.pr.menusandnav.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pr.menusandnav.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Home(
    modifier: Modifier = Modifier
) {
    // 보여 줄 더미데이터 나열
    val categories = listOf("Hits", "Happy", "WorkOut", "Running", "TGIF", "Yoga")
    val grouped = listOf("New Release", "Favorites", "Top Rated").groupBy { it[0] }

    LazyColumn(
        modifier = modifier
    ) {
        grouped.forEach {
            // recycler view의 대제목 설정을 위한 스티키 헤더
            stickyHeader {
                Text(text = it.value[0], modifier = modifier.padding(16.dp))
                LazyRow(
                    modifier = modifier
                ) {
                    items(categories) { category ->
                        BrowserItem(
                            cat = category,
                            drawable = R.drawable.ic_launcher_foreground
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BrowserItem(
    modifier: Modifier = Modifier,
    cat: String,
    drawable: Int
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .size(200.dp),
        border = BorderStroke(3.dp, color = Color.DarkGray)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cat)
            Image(painter = painterResource(id = drawable), contentDescription = null)
        }
    }
}

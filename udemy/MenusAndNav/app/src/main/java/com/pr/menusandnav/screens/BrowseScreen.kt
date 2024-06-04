package com.pr.menusandnav.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BrowseScreen(
    modifier: Modifier = Modifier
) {

    // 보여 줄 더미데이터 나열
    val categories = listOf(
        "Hits",
        "Happy",
        "WorkOut",
        "Running",
        "TGIF",
        "Yoga",
        "Running",
        "TGIF",
        "Yoga",
        "Running",
        "TGIF",
        "Yoga"
    )

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(categories) {
            BrowserItem(cat = it)
        }
    }
}

@Composable
fun BrowserItem(
    modifier: Modifier = Modifier,
    cat: String,
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
            Text(text = cat, modifier = modifier.padding(16.dp))
        }
    }
}

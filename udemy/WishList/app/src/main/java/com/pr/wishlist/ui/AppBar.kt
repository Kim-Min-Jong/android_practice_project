package com.pr.wishlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pr.wishlist.R

// 앱바 컴포저블
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    title: String,
    modifier: Modifier = Modifier,
    // 뒤로가기 클릭 시 불릴 콜백
    onBackNavClicked: () -> Unit = { },
) {
    // 표시 유무를 위해 변수로 분리
    val navigationIcon: (@Composable () -> Unit) = {
        IconButton(onClick = onBackNavClicked) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp), // 최대 24dp
            )
        },
        modifier = modifier.background(colorResource(id = R.color.app_bar_color)),
//        elevalation = 3.dp,
//        backgroundColor = colorResource(id = R.color.app_bar_color),
        navigationIcon = navigationIcon
    )

}

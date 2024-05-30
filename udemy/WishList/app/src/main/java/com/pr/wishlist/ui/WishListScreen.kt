package com.pr.wishlist.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pr.wishlist.data.DummyWish
import com.pr.wishlist.data.Wish

// scaffold를 사용하여 탑바 만들기
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // material design 의 기본 구성을 담고 있는 컴포저블
    Scaffold(
        //  탑바 생성
        topBar = {
            AppBarView(title = "WishList") {
                Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = modifier.padding(20.dp),
                contentColor = Color.White,
                containerColor = Color.Black,
                onClick = { /* navigae to add screen*/ }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(DummyWish.wishList) { wish ->
                WishItem(wish = wish) {
                    
                }
            }
        }
    }
}

@Composable
fun WishItem(
    modifier: Modifier = Modifier,
    wish: Wish,
    onClick: () -> Unit,
) {
    // CardView와 비슷한 컴포저블
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable {
                // 수정이나 작성하는 람다를 구체화
                onClick()
            },
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(text = wish.title, fontWeight = FontWeight.ExtraBold)
            Text(text = wish.description)
        }
    }
}

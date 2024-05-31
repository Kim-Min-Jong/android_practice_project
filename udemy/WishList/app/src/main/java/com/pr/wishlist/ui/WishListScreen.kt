package com.pr.wishlist.ui

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pr.wishlist.Screen
import com.pr.wishlist.WishViewModel
import com.pr.wishlist.data.Wish

// scaffold를 사용하여 탑바 만들기
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: WishViewModel
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
                onClick = { /* navigae to add screen*/
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        // collectAsState로 데이터 감지하고 flow에서 state로 변환
        val wishList = viewModel.getAllWishes.collectAsState(initial = emptyList())

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // 키를 등록함으로써 삭제 되었을 때, 리컴포지션이 일어나도록함 (키가 바뀌니까)
            items(wishList.value, key = { wish -> wish.id }) { wish ->
                // 아이템의 스와이프 동작 상태를 위한 state
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        // 양 옆으로 스와이프 하면
                        if (value == SwipeToDismissBoxValue.StartToEnd || value == SwipeToDismissBoxValue.EndToStart) {
                            // 컨텐츠를 삭제
                            viewModel.deleteWish(wish)
                        }
                        true
                    }
                )
                // 스와이프를 할 수 있도록 만들어주는 컴포저블
//                SwipeToDismissBox(state = dismissState, backgroundContent = ) {
//
//                }
                // 스와이프를 할 수 있도록 만들어주는 컴포저블 (예전거)
                SwipeToDismiss(
                    state = dismissState,
                    // 스와이프 시 각 아이템 뒤에 보여줄 배경 선언
                    background = {
                        // 스와이프 할 떄 색 변경 상태
                        val color by animateColorAsState(
                            targetValue = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent,
                            label = ""
                        )
                        val alignment = Alignment.CenterEnd
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp),
                            // 중간 끝에 배치
                            contentAlignment = alignment
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                    },
                    // 뱡향 가능 위치
                    directions = setOf(
                        SwipeToDismissBoxValue.StartToEnd,
                        SwipeToDismissBoxValue.EndToStart
                    ),
                    // 스와이프 했을때 없어질 것
                    dismissContent = {
                        val id = wish.id
                        navController.navigate(Screen.AddScreen.route + "/$id")
                    }
                )

                WishItem(wish = wish) {
                    val id = wish.id
                    // 특정 아이디의 detail view로 이동
                    navController.navigate(Screen.AddScreen.route + "/$id")
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

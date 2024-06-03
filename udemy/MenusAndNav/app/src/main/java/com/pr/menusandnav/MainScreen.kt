package com.pr.menusandnav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    // Scaffold의 상태 요소(탑바, 하단바 등)를 저장하는 변수

    // drawerState
    // material3 로 넘어오면서 scaffoldState가 없어지게 되고 scaffold의 각 요소 상태가 분리됨
    // drawerState를 따로 사용
    val scaffoldState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        // 상단바
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Drawer layout open
                        scope.launch {
                            scaffoldState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Text(text = "text", modifier = modifier.padding(paddingValues))
    }
}

// Drawerable Layout에 나타날 각 아이템 컴포저블
@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    // 선택 되었는지
    selected: Boolean,
    // 선택 된 아이템
    item: Screen.DrawerScreen,
    // 선택 된 아이템의 콜백
    onDrawerItemClicked: () -> Unit
) {

    val backgroundColor = if (selected) Color.DarkGray else Color.White
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(backgroundColor)
            .clickable {
                onDrawerItemClicked()
            }
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            modifier = modifier.padding(end = 8.dp, top = 4.dp)
        )
        Text(text = item.dTitle, style = MaterialTheme.typography.headlineSmall)
    }
}

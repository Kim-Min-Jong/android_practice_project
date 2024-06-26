package com.pr.menusandnav.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pr.menusandnav.MainViewModel
import com.pr.menusandnav.R
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

    // 현재 화면 위치를 나타낼 변수
    val controller = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 상단바 제목 유지 변수
    var title by remember { mutableStateOf("Home") }

    // 뷰모델 선언
    val viewModel: MainViewModel = viewModel()
    // 현재 화면위치를 위한 변수
    val currentScreen by remember {
        viewModel.currentScreen
    }

    // 다이얼로그를 열지 말지에 대한 변수
    val dialogOpen = remember { mutableStateOf(false) }

    // 바텀 바 컴포저블
    val bottomBar: @Composable () -> Unit = {
        if (currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            NavigationBar(
                modifier = modifier.wrapContentSize()
            ) {
                screensInBottom.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.bRoute,
                        onClick = {
                            // navController를 통한 탐색
                            controller.navigate(item.bRoute)
                            title = item.bTitle
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = item.bTitle) },
                    )
                }
            }

        }
    }

    // bottom sheet을 위한 변수들
    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }
    val maxSizeModifier = if (isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val modalSheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.PartiallyExpanded }
    )
    val roundedCornerRadius = if (isSheetFullScreen) 0.dp else 12.dp
    // 펼칠지 말지
    var openBottomSheet by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = scaffoldState,
        drawerContent = {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                items(screensInDrawer) { item ->
                    DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                        scope.launch {
                            scaffoldState.close()
                        }
                        if (item.dRoute == "add_account") {
                            // 추가 다이얼로그 열기
                            dialogOpen.value = true
                        } else {
                            controller.navigate(item.route)
                            title = item.dTitle
                        }
                    }
                }
            }
        }) {
        // mainContent

        Scaffold(
            // 하단 바
            bottomBar = bottomBar,
            // 상단바
            topBar = {
                TopAppBar(
                    title = { Text(text = title) },
                    // 탑바의 메뉴 버튼을 만들 수 있는 공간
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (modalSheetState.isVisible) {
                                    openBottomSheet = false
                                    modalSheetState.hide()
                                } else {
                                    openBottomSheet = true
                                    modalSheetState.show()
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Drawer layout open
                            scope.launch {
                                scaffoldState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null
                            )
                        }
                    },
                )
            },
        ) { paddingValues ->
            Navigation(
                navController = controller,
                viewModel = viewModel,
                paddingValues = paddingValues
            )

            AccountDialog(dialogOpen = dialogOpen)
        }
    }
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = modalSheetState,
            shape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius)
        ) {
            MoreBottomSheet(modifier)
        }
    }

}

@Composable
fun MoreBottomSheet(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_subscribe),
                    contentDescription = null,
                    modifier = modifier.padding(8.dp)
                )
                Text(text = "Subscribe")
            }
        }
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
        modifier = modifier
//            .fillMaxWidth()
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

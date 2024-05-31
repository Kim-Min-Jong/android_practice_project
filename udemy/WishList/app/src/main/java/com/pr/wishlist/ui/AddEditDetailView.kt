package com.pr.wishlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pr.wishlist.R
import com.pr.wishlist.WishViewModel
import com.pr.wishlist.data.Wish
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDetailView(
    modifier: Modifier = Modifier,
    id: Long,
    viewModel: WishViewModel,
    navController: NavHostController
) {
    var snackMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    // state
    val snackbarHostState = remember { SnackbarHostState() }
    // 리컴포지션시 update wish를 다시 가져옴
    // id가 0이 아니라면(아무것도없는 상태) 가져온 값을 반영
    if (id != 0L) {
        val wish = viewModel.getWishById(id).collectAsState(initial = Wish(0L))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }
    Scaffold(
        // 탑바 재사용
        topBar = {
            AppBarView(
                title = // 아이디에 따른 텍스트 변경
                if (id != 0L) stringResource(R.string.update_wish)
                else stringResource(R.string.add_wish)
            ) {
                // 이전으로 이동 (뒤로가기)
                navController.navigateUp()
            }
        },
        // 스낵바의 state를 등록
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = modifier.height(10.dp))

            // 입력 커스텀 필드
            WishTextFiled(
                label = "Title",
                value = viewModel.wishTitleState,
                onValueChanged = {
                    viewModel.onWishTitleChanged(it)
                }
            )
            WishTextFiled(
                label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChanged = {
                    viewModel.onWishDescriptionChanged(it)
                }
            )

            Spacer(modifier = modifier.height(10.dp))
            Button(onClick = {
                // 입력된 값이 있을 때만 업데이트 하기
                if (viewModel.wishTitleState.isNotEmpty() && viewModel.wishDescriptionState.isNotEmpty()) {
                    // add wish func
                    if (id != 0L) {
                        // update wish
                        viewModel.updateWish(
                            Wish(
                                id = id,
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                    } else {
                        // add wish
                        viewModel.addWish(
                            Wish(
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                        snackMessage = "adding complete"
                    }
                } else {
                    snackMessage = "Enter fileds to create a wish"
                }
                // 저장 or 업데이트가 끝나면 이전 페이지로 자동 이동하도록
                scope.launch {
                    // 스낵바를 띄움
                    snackbarHostState.showSnackbar(snackMessage)
                    navController.navigateUp()
                }
            }) {
                Text(
                    text = if (id != 0L) stringResource(R.string.update_wish)
                    else stringResource(R.string.add_wish),
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishTextFiled(
    // 호이스틩을 위해 매개변수로 분리
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = Color.Black) },
        // 어떤 종류의 키보드를 띄울 지
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        // 텍스트 색상
        colors = TextFieldDefaults.outlinedTextFieldColors(
            // 정의 된 색상
            textColor = Color.Black,
            // 커스텀으로 부분마다 바꿀 수도 있음
//            focusedBorderColor =
            // 등등...
        )
    )
}

package com.pr.wishlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pr.wishlist.R
import com.pr.wishlist.WishViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDetailView(
    modifier: Modifier = Modifier,
    id: Long,
    viewModel: WishViewModel,
    navController: NavHostController
) {
    Scaffold(
        // 탑바 재사용
        topBar = {
            AppBarView(
                title = // 아이디에 따른 텍스트 변경
                if (id != 0L) stringResource(R.string.update_wish)
                else stringResource(R.string.add_wish)
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = modifier.height(10.dp))


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

package com.pr.wishlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

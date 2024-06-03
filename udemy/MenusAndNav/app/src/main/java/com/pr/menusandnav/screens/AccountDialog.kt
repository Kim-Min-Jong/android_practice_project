package com.pr.menusandnav.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AccountDialog(
    modifier: Modifier = Modifier,
    // 다이얼로그를 열지 안열지
    dialogOpen: MutableState<Boolean>
) {
    if (dialogOpen.value) {
        // 다이얼로그 정의
        AlertDialog(
            onDismissRequest = {
                dialogOpen.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen.value = false
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogOpen.value = false
                    }
                ) {
                    Text(text = "Dismiss")
                }
            },
            title = {
                Text(text = "Add Account")
            },
            // 내부 커스텀 레이아웃 정의 가능
            text = {
                Column(
                    modifier = modifier
                        .wrapContentHeight()
                        .padding(top = 16.dp)
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        modifier = modifier.padding(top = 16.dp),
                        label = { Text(text = "Email") }
                    )
                    TextField(
                        value = "",
                        onValueChange = {},
                        modifier = modifier.padding(top = 16.dp),
                        label = { Text(text = "Password") }
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            shape = RoundedCornerShape(5.dp),
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
        )
    }
}

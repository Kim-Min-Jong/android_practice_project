package com.pr.chattingroom.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
) {
    // 작성한 텍스트 상태
    var text by remember { mutableStateOf("") }

    // 채팅 창 UI
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 채팅 메세지 보이기
        LazyColumn(
            modifier = modifier.weight(1f)
        ) {
            // TODO
        }

        // 챗 입력 및 버튼
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                modifier = modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            IconButton(
                onClick = {
                    // 버튼 클릭 시 메세지 전송
                    if (text.isNotEmpty()) {
                        // TODO
                        text = ""
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

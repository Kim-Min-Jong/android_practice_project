package com.pr.chattingroom.screen

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier
) {

    // 대화 다이얼로그를 보여줄지 말지하는 상태
    var showDialog by remember { mutableStateOf(false) }
    // 채팅방 이름 상태
    var name by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat Rooms", fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = modifier.height(16.dp))

        // 채팅방 목록
        LazyColumn {

        }
        Spacer(modifier = modifier.height(16.dp))
        // 새 방을 만드는 버튼
        Button(onClick = { showDialog = true }) {
            Text(text = "Create Room")
        }
    }
}

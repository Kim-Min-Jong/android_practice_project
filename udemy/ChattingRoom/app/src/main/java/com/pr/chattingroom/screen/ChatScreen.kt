package com.pr.chattingroom.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pr.chattingroom.R
import com.pr.chattingroom.data.Message
import com.pr.chattingroom.util.formatTimeStamp

@Composable
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

// 각 채팅을 보여줄 컴포저블
@RequiresApi(Build.VERSION_CODES.O)
@@Composable
fun ChatMessageItem(
    modifier: Modifier = Modifier,
    message: Message
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        // 상대방이냐 나냐에 따라 좌우 배치 변경
        horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = modifier
                .background(
                    // 상대방이냐 나냐에 따라 배경색 변경
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700)
                    else Color.Gray,
                    // 코너 둥글게
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
            Spacer(modifier = modifier.height(4.dp))
            // 보낸 사람 이름
            Text(
                text = message.senderFirstName,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            )
            // 보낸 시간
            Text(
                text = formatTimeStamp(message.timestamp), // Replace with actual timestamp logic
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            )
        }
    }
}

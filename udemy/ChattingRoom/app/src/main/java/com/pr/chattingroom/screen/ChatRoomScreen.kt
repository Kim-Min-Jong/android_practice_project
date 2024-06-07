package com.pr.chattingroom.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pr.chattingroom.RoomViewModel
import com.pr.chattingroom.data.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    roomViewModel: RoomViewModel = viewModel(),
    onJoinClicked: (Room) -> Unit
) {

    // 대화 다이얼로그를 보여줄지 말지하는 상태
    var showDialog by remember { mutableStateOf(false) }
    // 채팅방 이름 상태
    var name by remember { mutableStateOf("") }
    // 방 상태 옵저빙 (처음은 빈 상태)
    val rooms by roomViewModel.room.observeAsState(emptyList())

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
            items(rooms) {
                RoomItem(room = it, onJoinClicked = { onJoinClicked(it) })
            }
        }
        Spacer(modifier = modifier.height(16.dp))
        // 새 방을 만드는 버튼
        Button(onClick = { showDialog = true }) {
            Text(text = "Create Room")
        }

        // 상태에 따른 다이얼로그 출력
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = true },
                properties = DialogProperties()
            ) {
                Card(
                    shape = RoundedCornerShape(10)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 다이얼로그 제목
                        Text(
                            text = "Create a new room",
                            modifier = modifier.padding(8.dp)
                        )
                        // 본문
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = true,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        // button set
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    if (name.isNotBlank()) {
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text("Add")
                            }
                            Button(
                                onClick = { showDialog = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun RoomItem(
    room: Room,
    // 채팅방에 들어가는 콜백
    onJoinClicked: (Room) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = room.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
        OutlinedButton(
            onClick = { onJoinClicked(room) }
        ) {
            Text("Join")
        }
    }
}

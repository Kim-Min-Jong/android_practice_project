package com.pr.chattingroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pr.chattingroom.data.Result
import com.pr.chattingroom.data.Room
import com.pr.chattingroom.repository.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel: ViewModel() {

    private val _rooms = MutableLiveData<List<Room>>()
    // UI에서 접근할 변수
    val room: LiveData<List<Room>>
        get() = _rooms
    private val roomRepository = RoomRepository(Injection.getInstance())

    // 방 생성
    fun createRoom(name: String) {
        viewModelScope.launch {
            roomRepository.createRoom(name)
        }
    }

    // 방 목록 가져오기
    fun loadRooms() {
        viewModelScope.launch {
            when(val result = roomRepository.getRooms()) {
                is Result.Success -> {
                    _rooms.value = result.data
                }
                is Result.Error -> {

                }
            }
        }
    }
}

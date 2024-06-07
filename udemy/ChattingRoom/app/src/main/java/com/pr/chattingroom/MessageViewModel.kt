package com.pr.chattingroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pr.chattingroom.data.Message
import com.pr.chattingroom.data.Result
import com.pr.chattingroom.data.User
import com.pr.chattingroom.repository.MessageRepository
import com.pr.chattingroom.repository.UserRepository
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageRepository = MessageRepository(Injection.getInstance())
    private val userRepository = UserRepository(FirebaseAuth.getInstance(), Injection.getInstance())

    init {
        loadCurrentUser()
    }

    // UI에 연동될 변수들
    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>>
        get() = _messages

    private val _currentUser: MutableLiveData<User> = MutableLiveData()
    val currentUser: LiveData<User>
        get() = _currentUser
    private val _roomId = MutableLiveData<String>()
    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {
                    // Handle error, e.g., show a snackbar
                }

            }
        }
    }

    // 채팅방의 메세지를 불러옴
    fun loadMessages() {
        viewModelScope.launch {
            if (_roomId != null) {
                messageRepository.getChatMessage(_roomId.value.toString())
                    // message에서 수집
                    .collect { _messages.value = it }
            }
        }
    }

    fun sendMessage(text: String) {
        // 유저가 존재한다면
        if (_currentUser.value != null) {
            // 유저의 정보를 담아 메세지 객체를 만듦
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = text
            )
            viewModelScope.launch {
                // 메세지를 firestore에 저장
                when (messageRepository.sendMessage(_roomId.value.toString(), message)) {
                    is Result.Success -> Unit
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    // 현재 채팅룸 정보 설정
    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }
}

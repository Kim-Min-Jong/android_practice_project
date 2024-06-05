package com.pr.chattingroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.pr.chattingroom.data.Result
import com.pr.chattingroom.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    // repository 초기화
    private val userRepository: UserRepository = UserRepository(
        FirebaseAuth.getInstance(),
        Injection.getInstance()
    )

    // 회원가입 결과를 통해 UI를 바꿀 livedata
    private val _authResult: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val authResult: LiveData<Result<Boolean>>
        get() = _authResult

    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(
                email,
                password,
                firstName,
                lastName
            )
        }
    }
}

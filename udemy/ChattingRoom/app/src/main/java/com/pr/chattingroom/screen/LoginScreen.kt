package com.pr.chattingroom.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pr.chattingroom.AuthViewModel
import com.pr.chattingroom.data.Result

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    // 회원가입 화면으로 이동을 위한 탐색 변수
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel,
    // 로그인 성공시 실행 될 콜백
    onSignInSuccess: () -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // authResult를 observing 하는 변수
    val result by authViewModel.authResult.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            // 텍스트를 적을 때 비밀번호 형식으로 보여주기
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                authViewModel.login(email, password)
                // 로그인 상태별 로직 분기
                when (result) {
                    is Result.Success -> {
                        onSignInSuccess()
                    }

                    is Result.Error -> {
                        // TODO
                    }

                    else -> {
                        // TODO
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Login")
        }
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = "Don't have an account? Sign up.",
            modifier = modifier.clickable {
                onNavigateToSignUp()
            }
        )
    }
}

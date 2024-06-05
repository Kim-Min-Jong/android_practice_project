package com.pr.chattingroom.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.pr.chattingroom.data.Result
import com.pr.chattingroom.data.User

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    // 한 번 더 추상화하면 datasource 단으로

    // 회원가입 메소드
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName, lastName, email)
            saveUserToFirestore(user)
            // 성공 시
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // 사용자 저장 메소드
    private suspend fun saveUserToFirestore(user: User) {
        // 파이어스토어 컬렉선에 유저정보를 저장
        firestore.collection("users")
            .document(user.email)
            .set(user)
            .await()
    }
}

package com.pr.chattingroom.data

// 네트워크 응답 상태를 가지는 sealed class
sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Exception): Result<Nothing>()
}

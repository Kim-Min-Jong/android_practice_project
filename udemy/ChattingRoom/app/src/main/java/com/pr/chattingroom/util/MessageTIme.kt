package com.pr.chattingroom.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// 메세지 보낸 시간 표시
// Long타입의 시간을 문자열 형식으로 변환
@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeStamp(
    timeStamp: Long
): String {
    // timeStamp와 지역에 따라 시간으로 ㅂ녀환
    val messageDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timeStamp),
        ZoneId.systemDefault()
    )

    val now = LocalDateTime.now()

    // 같은 날인 지 반별하여 어제, 오늘을 표시 및 시간 표시
    return when {
        isSameDay(messageDateTime, now) -> "today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1), now) -> "yesterday ${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }
}

// 두 시간을 비교해 같은 날인 지 판별
@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(
    dateTime1: LocalDateTime,
    dateTime2: LocalDateTime
): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter) == dateTime2.format(formatter)
}

// HH:mm 형식으로 변환
@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return formatter.format(dateTime)
}

// 0X XX XXXX 날짜 형식으로 변환
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return formatter.format(dateTime)
}

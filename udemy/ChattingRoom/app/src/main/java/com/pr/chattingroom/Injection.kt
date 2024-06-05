package com.pr.chattingroom

import com.google.firebase.firestore.FirebaseFirestore

// 뷰모델에 repository를 주입하기 위한 object
// 의존성 주입 라이브러리인 dagger, hilt 로 대체 가능
object Injection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getInstance(): FirebaseFirestore {
        return instance
    }
}

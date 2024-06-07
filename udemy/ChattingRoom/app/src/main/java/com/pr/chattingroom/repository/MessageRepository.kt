package com.pr.chattingroom.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pr.chattingroom.data.Message
import com.pr.chattingroom.data.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepository (
    private val firestore: FirebaseFirestore
){

    // 메세지 전송 메소드
    suspend fun sendMessage(
        roomId: String,
        message: Message
    ) = try {
        firestore.collection("rooms").document(roomId)
            .collection("messages").add(message).await()
    } catch (e: Exception) {
        Result.Error(e)
    }

    // 메세지를 가져오는 메소드
    fun getChatMessage(roomId: String): Flow<List<Message>> = callbackFlow {
        val subscription = firestore.collection("rooms").document(roomId)
            .collection("messages")
            // 시간 순 정렬
            .orderBy("timestamp")
            // 가져온 Snapshot을 다룸
            .addSnapshotListener { value, error ->
                value?.let {
                    // flow로 보냄
                    trySend(
                        it.documents.map { document ->
                            document.toObject(Message::class.java)!!.copy()
                        }
                    ).isSuccess
                }
            }
        // 다끝나면 flow를 종료
        awaitClose{ subscription.remove() }
    }
}

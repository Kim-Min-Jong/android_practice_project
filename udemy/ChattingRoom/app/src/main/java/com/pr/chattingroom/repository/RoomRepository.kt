package com.pr.chattingroom.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pr.chattingroom.data.Room
import kotlinx.coroutines.tasks.await
import  com.pr.chattingroom.data.Result

class RoomRepository(
    private val firestore: FirebaseFirestore
) {
    // 파이어베이스에 새로운 방(컬렉션)을 만듦
    suspend fun createRoom(name: String): Result<Unit> {
        return try {
            val room = Room(name = name)
            firestore.collection("rooms").add(room).await()
           Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // 생성되어 있는 방 목록을 가져옴
    suspend fun getRooms(): Result<List<Room>> = try {
        // rooms 컬렉션 내 모두를 가져옴
        val querySnapshot = firestore.collection("rooms").get().await()

        // 가져온 리스트를 Room 형태로 변환 (collection id 추가)
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Room::class.java)!!.copy(id = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Result.Error(e)
    }
}

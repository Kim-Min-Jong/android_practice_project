package com.example.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.launch;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ApiService {
    @POST("message")
    suspend fun sendMessage(@Body message: MessageBody)
}

data class MessageBody(val content: String)

class MessageService : Service() {
    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://your.api.server/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val binder = object : IMessageService.Stub() {
        override fun sendMessage(message: String) {
            Log.d("MessageService", "메시지 수신: $message")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    api.sendMessage(MessageBody(message))
                    Log.d("MessageService", "서버 전송 성공")
                } catch (e: Exception) {
                    Log.e("MessageService", "서버 전송 실패", e)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}

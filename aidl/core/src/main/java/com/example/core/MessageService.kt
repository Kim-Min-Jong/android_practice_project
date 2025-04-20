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
            Log.d("MessageService", "Received: $message")
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    api.sendMessage(MessageBody(message))
                    Log.d("MessageService", "Message sent to server")
                } catch (e: Exception) {
                    Log.e("MessageService", "Error sending message", e)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}

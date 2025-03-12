package com.pr.service_pr

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.widget.Toast


class MyMessengerIPCService : Service() {

    private lateinit var messenger: Messenger

    // 핸들러를 생성
    // 메세지 송수신 역할
    internal class IncomingHandler(
        service: Service,
        private val context: Context = service.applicationContext,
    ): Handler(Looper.getMainLooper()) { // Main Looper의 메세지를 가져옴

        private val clients = mutableListOf<Messenger>()

        // 메서지를 받아 메세지에 따른 조건 분기 처리
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                // replyTo -> 넘어온 클라이언트
                MSG_BIND_CLIENT -> clients.add(msg.replyTo)
                MSG_UNBIND_CLIENT -> clients.remove(msg.replyTo)
                MSG_SHOW_TOAST -> context.showSimpleToast(msg.data.getString(MSG_TOAST_TEXT, "no text"))
                MSG_ADD_REQUEST -> add(msg.arg1, msg.arg2)
                else -> super.handleMessage(msg)
            }
        }

        private fun add(n1: Int, n2: Int) {
            val message = Message.obtain(
                null,
                MSG_ADD_RESPONSE,
                n1 + n2,
                0
            )

            clients.forEach {
                it.send(message)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        // messenger를 만들고 binder를 반환
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }

    // message의 what이나 data에 담길 상수를 정의
    companion object {
        const val MSG_BIND_CLIENT = 2
        const val MSG_UNBIND_CLIENT = 3
        const val MSG_SHOW_TOAST = 1
        const val MSG_ADD_REQUEST = 4
        const val MSG_ADD_RESPONSE = 5
        const val MSG_TOAST_TEXT = "toast_text"
    }
}

fun Context.showSimpleToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

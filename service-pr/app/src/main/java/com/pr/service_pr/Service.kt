package com.pr.service_pr

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class Service : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 서비스가 어떤 쓰레드에서 실행되는지 확인
        Log.e(TAG, Thread.currentThread().name)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        Log.e(TAG, "servcie create")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.e(TAG, "service destroy")
        super.onDestroy()
    }

    companion object {
        const val TAG = "TAG"
    }
}

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
        // 메인 쓰레드가 찍힘
        // 메인 쓰레드에서 실행시 ANR을 조심해야함
        // 그래서 새로운 쓰레드에서 실행되는게 좋음

        // 서비스는 시스템에 의해 강제종료 될 수 있음
        // 재시작 정책을 설정해주어야함

        // onStartCommand에서 플래그에 따라 방식이 달라짐
        // START_NOT_STICKY: 서비스를 명시적으로 다시 시작할 때 까지 만들지 않음
        // START_STICKY: 서비스를 다시 만들지만 마지막 Intent를 onStartCommand의 인자로 다시 전달하지 않음
        // 이는 일단 계속 살아있어야되지만 별다른 동작이 필요하지 않은 음악앱같은 서비스에 적절
        // START_REDELIVER_INTENT: 이름에서 알겠듯이 마지막 Intent를 onStartCommand의 인자로 다시 전달
        // 즉각적인 반응이 필요한 파일 다운로드 서비스 같은 곳에 적합


//        return super.onStartCommand(intent, START_FLAG_REDELIVERY, startId)
//        return super.onStartCommand(intent, START_FLAG_RETRY, startId)
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

package com.pr.service_pr

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast

// binder를 bind해주는 서비스
class MyBinderService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return MyBinder()
    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    // binder 클래스를 만들어주고
    inner class MyBinder: Binder() {
        // 서비스 객체 그 자체를 반환
        val service: MyBinderService
            get() = this@MyBinderService
    }
}

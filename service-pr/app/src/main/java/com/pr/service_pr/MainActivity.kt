package com.pr.service_pr

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pr.service_pr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var bindServiceBinder: MyBinderService.MyBinder? = null

    // 서비스와 연결을 모니터링하는 connection 객체
    private val bindServiceConnection = object: ServiceConnection {
        // 서비스가 연결될 때 불리는 콜백
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            bindServiceBinder = p1 as MyBinderService.MyBinder
        }
        // 서비스가 해제 될 떄 불리는 콜백
        override fun onServiceDisconnected(p0: ComponentName?) {
            Toast.makeText(this@MainActivity, "unbounded", Toast.LENGTH_SHORT).show()
            bindServiceBinder = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()
    }

    private fun setButtonListeners() {
        // foreground
        binding.startService.setOnClickListener {
            startBasicService()
        }

        binding.stopService.setOnClickListener {
            stopBasicService()
        }

        binding.startForegroundService.setOnClickListener {
            startForegroundService()
        }

        // bind service
        binding.bindService.setOnClickListener {
            bindBinderService()
        }
        binding.unbindService.setOnClickListener {
            unbindBinderService()
        }
        binding.showToastBinder.setOnClickListener {
            println(bindServiceBinder)
            if (bindServiceBinder?.isBinderAlive == true) {
                bindServiceBinder?.run {
                    service.showToast("Service is bounded")
                }
            } else {
                println("Service is unbounded")
            }
        }
    }

    private fun bindBinderService() {
        // 인텐트를 통해 서비스를 연결하고
        Intent(this, MyBinderService::class.java).run {
            // service connection을 토해 서비스를 바인드 함
            bindService(this, bindServiceConnection, android.app.Service.BIND_AUTO_CREATE)
        }
    }
    private fun unbindBinderService() {
        unbindService(bindServiceConnection)
    }


    private fun startForegroundService() {
        Intent(this, MyForegroundService::class.java).run {
            // foreground service 정책생 api 26 이상일 떈 아래 메소드를 호출
            // 아니면 일반 실행 함수 호출
            if (Build.VERSION.SDK_INT > VERSION_CODES.O) {
                startForegroundService(this)
            } else {
                startService(this)
            }
        }
    }

    private fun startBasicService() {
        // 인텐트를 통해 서비스를 시작
        Intent(this, Service::class.java).run {
            startService(this)
        }
    }

    private fun stopBasicService() {
        // 인텐트를 통해 서비스를 정지
        Intent(this, Service::class.java).run {
            stopService(this)
        }

    }


}

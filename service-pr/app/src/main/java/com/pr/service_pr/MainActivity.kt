package com.pr.service_pr

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pr.service_pr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.startService.setOnClickListener {
            startBasicService()
        }

        binding.stopService.setOnClickListener {
            stopBasicService()
        }

        binding.startForegroundService.setOnClickListener {
            startForegroundService()
        }
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

package com.pr.service_pr

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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

    private val messengerIPCHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when (msg.what){
                MyMessengerIPCService.MSG_ADD_RESPONSE -> {
                    showSimpleToast("Add response: ${msg.arg1}")
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    // 클라이언트 측 Messenger
    private val messengerIPCClient = Messenger(messengerIPCHandler)
    // 서비스 측 Messenger
    private var messengerIPCService: Messenger? = null
    // 위 둘을 연결하기 위한 서비스 커넥션
    private val messengerIPCServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            // 서비스측 메신저를 만들고
            messengerIPCService = Messenger(p1).apply {
                // 메세지를 보냄
                send(Message.obtain(
                    null,
                    MyMessengerIPCService.MSG_BIND_CLIENT,
                    0,
                    0
                ).apply {
                    // 메세지는 클라이언트 측 메신저 (메세지 포함)
                    replyTo = messengerIPCClient
                })
            }
            showSimpleToast("MessengerIPCService - onServiceConnected")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            messengerIPCService = null
            showSimpleToast("MessengerIPCService - onServiceDisconnected")
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

        // bind messenger
        binding.bindMessenger.setOnClickListener {
            bindMessengerService()
        }
        binding.unbindMessenger.setOnClickListener {
            unbindMessengerService()
        }
        binding.showToastMessenger.setOnClickListener {
            showMessengerIPCServiceToast()
        }
        binding.invokeFunctionMessenger.setOnClickListener {
            invokeAddMessengerIPCService()
        }
    }

    private fun bindMessengerService() {
        // 서비스 커넥션을 통해 서비스를 bind
        Intent(this, MyMessengerIPCService::class.java).run {
            bindService(this, messengerIPCServiceConnection, android.app.Service.BIND_AUTO_CREATE)
        }
    }

    private fun unbindMessengerService() {
        // 서비스에 unbind 신호를 보낸후
        messengerIPCService?.send(
            Message.obtain(
                null,
                MyMessengerIPCService.MSG_UNBIND_CLIENT,
                0
                ,0
            ).apply {
                replyTo = messengerIPCClient
            }
        )
        // 서비스를 해제
        unbindService(messengerIPCServiceConnection)
    }

    // toast를 띄우기 위해 서비스에 메세지를 전송
    private fun showMessengerIPCServiceToast() {
        // 서비스에 객체를 보냄
        messengerIPCService?.send(
            // 메세지를
            Message.obtain(
                null,
                // 플래그를 보고 서비스 핸들러에서 분기 처리
                MyMessengerIPCService.MSG_SHOW_TOAST,
                0
                ,0
            ).apply {
                // 이런 데이터를 포함해서
                data = bundleOf(MyMessengerIPCService.MSG_TOAST_TEXT to "Messenger IPC Service!")
            }
        )
    }

    // 서비스의 함수 호출
    private fun invokeAddMessengerIPCService() {
        messengerIPCService?.send(Message.obtain(null, MyMessengerIPCService.MSG_ADD_REQUEST, 5, 1))
    }
// ----------------------------------------
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

// ----------------------------------------

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

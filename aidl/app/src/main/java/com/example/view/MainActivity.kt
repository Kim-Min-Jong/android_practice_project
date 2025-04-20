package com.example.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.core.IMessageService;

class MainActivity : AppCompatActivity() {

    private var messageService: IMessageService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            messageService = IMessageService.Stub.asInterface(service)
            isBound = true
            Log.d("MainActivity", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
            messageService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        val intent = Intent("com.example.core.IMessageService")
        intent.setPackage("com.example.core")
        bindService(intent, connection, BIND_AUTO_CREATE)

        sendButton.setOnClickListener {
            val message = editText.text.toString()
            if (isBound) {
                messageService?.sendMessage(message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
        }
    }
}

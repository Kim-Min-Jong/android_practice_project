package com.pr.service_pr

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread

class MyForegroundService : Service() {
    // notification을 통해 foreground service를 보여줌
    private val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private fun registerDefaultNotificationChannel() {
        // notification은 api 26이상이면 채널을 따로 생성해 주어야함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createDefaultNotificationChannel())
        }
    }

    private fun createDefaultNotificationChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = CHANNEL_DESCRIPTION
            setShowBadge(true)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }
    }

    override fun onCreate() {
        registerDefaultNotificationChannel()
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 서비스가 실행되면 foreground를 실행 -> 노티 실행
        startForeground(NOTIFICATION_DOWNLOAD_ID, createDownloadNotification(0))

        thread {
            // 반복문을 돌며 상태를 계속 noti
            for (i in 0 until 100) {
                Thread.sleep(100)
                // 더미 업데이트바 
                updateProgress(i)
            }
            // 매번 업데이트 하기위해 foreground를 멈춰주어야하는데 boolean 값으로 noti ui을 유무를 정할 수 있음
            stopForeground(true)
            stopSelf()
            notificationManager.notify(NOTIFICATION_COMPLETE_ID, createCompleteNotification())
        }

        return START_STICKY
    }

    private fun updateProgress(i: Int) {
        notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, createDownloadNotification(i))
    }

    private fun createDownloadNotification(progress: Int): Notification? =
        // noti 생성
        NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Download video...")
            setContentText("Wait!")
            setSmallIcon(R.drawable.ic_launcher_background)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            // noti 터치 시 넘어올 인텐트(화면) 설정
            setContentIntent(
                PendingIntent.getActivity(
                    this@MyForegroundService,
                    0,
                    Intent(this@MyForegroundService, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        }.build()

    private fun createCompleteNotification(): Notification? =
        NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Download complete!")
            setContentText("Nice 🚀")
            setSmallIcon(R.drawable.ic_launcher_background)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            setContentIntent(
                PendingIntent.getActivity(
                    this@MyForegroundService,
                    0,
                    Intent(this@MyForegroundService, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        }.build()


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    companion object {
        private const val NOTIFICATION_DOWNLOAD_ID = 1
        private const val NOTIFICATION_COMPLETE_ID = 2
        private const val CHANNEL_ID = "my_channel"
        private const val CHANNEL_NAME = "default"
        private const val CHANNEL_DESCRIPTION = "This is default notification channel"
    }
}

package com.example.libsysmanagement.session

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.libsysmanagement.R
import com.example.libsysmanagement.ui.MainActivity

class SessionTrackerService : Service() {
    companion object {
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val FOREGROUND_SERVICE_DATA = "service_data"
        private const val CHANNEL_ID = "SessionTrackerService123"
        private const val CHANNEL_NAME = "Session Tracker Service"
        private const val NOTIFICATION_ID = 1
    }

    private var startTime: Long = 0L

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { mIntent ->
            val action = mIntent.action
            action?.let { mAction ->
                when (mAction) {
                    ACTION_START_FOREGROUND_SERVICE -> {
                        intent.extras?.let {
                            startTime = it.getLong(FOREGROUND_SERVICE_DATA)
                        }
                        startForegroundService()
                    }
                    ACTION_STOP_FOREGROUND_SERVICE -> stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) createNotificationChannel()
        else createNotificationBelowOreo()
    }

    private fun createNotificationBelowOreo() {
        val notification = NotificationCompat
            .Builder(this, CHANNEL_ID).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                priority = Notification.PRIORITY_MAX
                setWhen(startTime)
                setUsesChronometer(true)
                setContentTitle("Your session is running")
                setContentIntent(getCategoryIntent())
            }.build()
        startForeground(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                .apply {
                    lightColor = Color.BLUE
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .apply {
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher)
                setWhen(startTime)
                setUsesChronometer(true)
                setContentTitle("Your session is running")
                priority = NotificationManager.IMPORTANCE_DEFAULT
                setCategory(Notification.CATEGORY_SERVICE)
                setContentIntent(getCategoryIntent())
            }.build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, notification)
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
        startActivity()
    }

    private fun getCategoryIntent(): PendingIntent {
        val intent = Intent(this, SessionTrackerService::class.java)
            .apply { action = ACTION_STOP_FOREGROUND_SERVICE }

        return PendingIntent.getService(
            this,
            123,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK }
        startActivity(intent)
    }
}

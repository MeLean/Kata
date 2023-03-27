package com.milen.kata.experiments.foregroundservice

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.milen.kata.R
import kotlinx.coroutines.*


class MyCountingService : Service() {
    private lateinit var notificationManager: NotificationManagerCompat
    private var counter = 0
    private var isServiceRunning = false

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        when {
            intent?.action == ACTION_STOP_SERVICE -> {
                stopService()

                START_NOT_STICKY
            }

            isServiceRunning.not() -> {
                isServiceRunning = true
                startForeground(NOTIFICATION_ID, createNotification())

                scope.launch {
                    while (isServiceRunning) {
                        delay(timeMillis = DEFAULT_WAITING_MILLISECONDS)
                        counter++

                        if (isActive) {
                            withContext(Dispatchers.Main) {
                                notificationManager.notify(
                                    NOTIFICATION_ID,
                                    createNotification()
                                )
                            }
                        }
                    }
                }

                START_STICKY
            }

            else -> START_STICKY
        }


    override fun onDestroy() {
        super.onDestroy()
        stopService()
        // todo save data
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        notificationManager.createNotificationChannelIfNeeded(
            name = getString(R.string.channel_name),
            descriptionText = getString(R.string.channel_description)
        )

        val cancelAction = NotificationCompat.Action.Builder(
            R.drawable.ic_cancel,
            getString(android.R.string.cancel),
            cancelPendingIntent()
        ).build()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.counting_service_name))
            .setContentText(getString(R.string.counter_, counter))
            .setSmallIcon(R.drawable.ic_update)
            .setOnlyAlertOnce(true)
            .addAction(cancelAction)
            .build()
    }


    private fun cancelPendingIntent(): PendingIntent =
        PendingIntent.getService(
            this,
            REQUEST_CODE,
            Intent(this, MyCountingService::class.java).apply { action = ACTION_STOP_SERVICE },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun stopService() {
        isServiceRunning = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }

        stopSelf()
    }

    companion object {
        private const val NOTIFICATION_ID = 777
        private const val DEFAULT_WAITING_MILLISECONDS = 1000L // 1 second
        private const val ACTION_STOP_SERVICE = "com.milen.kata.STOP_SERVICE"
        private const val REQUEST_CODE = 0

        fun startForeground(context: Context, extras: Bundle? = null) {
            Intent(context, MyCountingService::class.java).apply {
                extras?.let { putExtras(it) }
            }.also {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                        context.startForegroundService(it)

                    else -> context.startService(it)
                }
            }
        }
    }
}

private fun NotificationManagerCompat.createNotificationChannelIfNeeded(
    name: String,
    descriptionText: String,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
                enableVibration(false)
            })
    }
}
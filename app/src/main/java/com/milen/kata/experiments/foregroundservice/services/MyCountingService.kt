package com.milen.kata.experiments.foregroundservice.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.milen.kata.R
import com.milen.kata.experiments.foregroundservice.CHANNEL_ID
import com.milen.kata.experiments.foregroundservice.UNKNOWN
import com.milen.kata.experiments.foregroundservice.WIFI
import com.milen.kata.experiments.foregroundservice.data.MeasurementTask
import com.milen.kata.utils.DebugLogger
import kotlinx.coroutines.*

class MyCountingService(
    private val measurementManager: MeasurementManager = MeasurementManager()
) : Service() {
    private lateinit var notificationManager: NotificationManagerCompat
    private var counter = 0
    private var isServiceRunning = false

    private val scope = CoroutineScope(Dispatchers.Default)

    private val measurementTask = MeasurementTask()

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        when {
            intent?.action == ACTION_STOP_SERVICE -> {
                stopService()

                START_NOT_STICKY
            }

            isServiceRunning.not() -> {
                isServiceRunning = true
                val measurement = doMeasurementRecord()
                startForeground(NOTIFICATION_ID, createNotification(measurement))

                scope.launch {
                    while (isServiceRunning) {
                        delay(timeMillis = DEFAULT_WAITING_MILLISECONDS)
                        if (isActive) {
                            withContext(Dispatchers.Main) {
                                doMeasurementRecord().takeIf { it.contains(UNKNOWN).not() }?.let {
                                    measurementTask.measurementData.add(it)
                                    DebugLogger.log("counter: $counter : $it")

                                    counter++
                                    notificationManager.notify(
                                        NOTIFICATION_ID,
                                        createNotification(it)
                                    )
                                } ?: DebugLogger.log("counter: $counter : UNKNOWN measurement!")
                            }
                        }
                    }
                }

                START_STICKY
            }

            else -> START_STICKY
        }

    private fun doMeasurementRecord(): String =
        with(measurementManager) {
            val networkType = getNetworkType(this@MyCountingService)
            val networkSpeedTypeStr = networkType.takeIf { it != WIFI }?.let {
                ": ${getNetworkSpeedType(this@MyCountingService)}"
            }.orEmpty()

            "$networkType$networkSpeedTypeStr"
        }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        DebugLogger.log("${javaClass.simpleName}.onTaskRemoved() called!")
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLogger.log("${javaClass.simpleName}.onDestroy() called!")
        stopService()
        // todo save data
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(measurement: String): Notification {

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
            .setContentText(getString(R.string.measured_label_, counter, measurement))
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
        internal const val ACTION_STOP_SERVICE = "com.milen.kata.STOP_SERVICE"
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
package com.milen.kata.experiments.foregroundservice.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.milen.kata.R
import com.milen.kata.experiments.foregroundservice.CHANNEL_ID
import com.milen.kata.experiments.foregroundservice.data.MeasurementEntity
import com.milen.kata.experiments.foregroundservice.data.MeasurementTask
import com.milen.kata.utils.DebugLogger
import kotlinx.coroutines.*

class MyCountingService : Service() {
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var networkMeasurementService: NetworkMeasurementService
    private var counter = 0
    private var isServiceRunning = false

    private val scope = CoroutineScope(Dispatchers.Default)

    private val measurementTask = MeasurementTask()

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        networkMeasurementService = NetworkMeasurementService(
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager,
            getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        when {
            intent?.action == ACTION_STOP_SERVICE -> {
                stopService()

                START_NOT_STICKY
            }

            isServiceRunning.not() -> {
                isServiceRunning = true

                doMeasurementRecord().run {
                    startForeground(
                        NOTIFICATION_ID, createNotification(
                            networkType,
                            networkSpeedType
                        )
                    )
                }


                scope.launch {
                    while (isServiceRunning) {
                        delay(timeMillis = DEFAULT_WAITING_MILLISECONDS)
                        if (isActive) {
                            withContext(Dispatchers.Main) {
                                doMeasurementRecord().run {
                                    measurementTask.measurementData.add(this)
                                    DebugLogger.log("counter: $counter : $this")

                                    counter++
                                    notificationManager.notify(
                                        NOTIFICATION_ID,
                                        createNotification(
                                            networkType,
                                            networkSpeedType
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                START_STICKY
            }

            else -> START_STICKY
        }

    private fun doMeasurementRecord(): MeasurementEntity =
        with(networkMeasurementService) {
            MeasurementEntity(
                operatorName = getNetworkOperatorName(),
                mcc = getMcc(),
                mnc = getMnc(),
                rPlmn = getPlmn(),
                roam = getRoam(),
                nodeBid = getNodeBId(),
                lcid = getLcid(),
                eci = getEci(),
                tac = getTac(),
                pci = getPci(),
                networkSpeedType = getNetworkSpeedType(),
                networkType = getNetworkType(),
                frequency = getFrequency(),
                rfcn = getRfcn(),
                bWs = getBWs(),
                bw = getCurrentBandwidth(),
                bandIdentifier = getBandIdentifier(),
                hasEndc = isEndcAvailable(),
                sessionState = getSessionManagementState(),
                rfcnEu = getRfcn(), //todo check if it is the same?
                plci = getPci(), //todo check if it is the same?
                rsrp = getRsrp(),
                rsrq = getRsrq(),
                rssi = getRssi(),
                snr = getSnr(),
                cqi = getCqi(),
                tabn = getTabn(),
                ul = getUl(),
                dl = getDl(),
                ssrsrp = getSsrsrp(),
                ssrsrq = getSsrsrq(),
                csirsrp = getCsirsrp(),
                csirsrq = getCsirsrq(),
                csirssi = getCsirssi(),
                nrrssi = getNrrssi(),
                csisinr = getNrrssi(), // todo check if they are the same
                sssinr = getSssinr(),
                csisinratio = getCsisinratio()
            )
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

    private fun createNotification(networkType: String, networkSpeedType: String): Notification {

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
            .setContentText(
                getString(
                    R.string.measured_label_,
                    counter,
                    "$networkType: $networkSpeedType"
                )
            )
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
        private const val DEFAULT_WAITING_MILLISECONDS = 2000L // 2 seconds
        internal const val ACTION_STOP_SERVICE = "com.milen.kata.STOP_SERVICE"
        private const val REQUEST_CODE = 0

        fun startForeground(context: Context, extras: Bundle? = null) {
            Intent(context, MyCountingService::class.java).apply {
                extras?.let { putExtras(it) }
            }.also {
                context.startForegroundService(it)
            }
        }
    }
}


private fun NotificationManagerCompat.createNotificationChannelIfNeeded(
    name: String,
    descriptionText: String,
) {
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
package com.milen.kata.experiments.foregroundservice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.milen.kata.R
import com.milen.kata.experiments.foregroundservice.services.MyCountingService
import com.milen.kata.utils.isServiceRunning
import com.milen.kata.utils.showAlert

class ForegroundServiceExperimentActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val notifyPermissionStr = Manifest.permission.POST_NOTIFICATIONS

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // for sake of experiment
            when (isGranted) {
                true -> tryToStartService()
                else -> showNotificationPermissionAlert()
            }
        }

    private val networkReadingPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    private val networkReadingPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            tryToStartService()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground_service_experiment)
    }

    override fun onResume() {
        super.onResume()
        //for the sake of experiment
        when (isServiceRunning(MyCountingService::class.java.canonicalName.orEmpty())) {
            true -> applyStopServiceUi()
            else -> applyStartServiceUi()
        }
    }

    private fun applyStartServiceUi() {
        findViewById<Button>(R.id.btn_toggle_service).apply {
            text = getString(R.string.start_service)
            setOnClickListener {
                // for the sake of the experiment
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    isPermissionNotGranted(notifyPermissionStr)
                ) {
                    permissionLauncher.launch(notifyPermissionStr)
                } else {
                    tryToStartService()
                }
            }
        }
    }

    private fun applyStopServiceUi() {
        findViewById<Button>(R.id.btn_toggle_service).apply {
            text = getString(R.string.stop_service)
            setOnClickListener {
                Intent(this@ForegroundServiceExperimentActivity, MyCountingService::class.java)
                    .apply {
                        action = MyCountingService.ACTION_STOP_SERVICE
                        startService(this)
                    }

                showAlert(
                    msg = getString(R.string.foreground_service_stopped),
                    iconRes = R.drawable.ic_cancel,
                    onOkClicked = { applyStartServiceUi() }
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openNotificationSettingsScreen(): Unit =
        with(Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }) {
            startActivity(this)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotificationPermissionAlert() {
        showAlert(
            msg = getString(R.string.notification_permission_required),
            onOkClicked = { openNotificationSettingsScreen() },
            onCancelClicked = { finishAffinity() }
        )
    }

    private fun tryToStartService(): Unit =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && areNotificationsDisabled()) {
            showNotificationPermissionAlert()
        } else {
            startNotificationPermittedService()
        }

    private fun startNotificationPermittedService() {
        for (permission in networkReadingPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                networkReadingPermissionsLauncher.launch(permission)
                return
            }
        }

        MyCountingService.startForeground(context = this)
        showAlert(
            msg = getString(R.string.foreground_service_started),
            iconRes = R.drawable.ic_update,
            onOkClicked = { applyStopServiceUi() }
        )
    }
}
private fun Context.showToast(msg: String): Unit =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

@RequiresApi(Build.VERSION_CODES.O)
private fun Context.areNotificationsDisabled(): Boolean =
    NotificationManagerCompat.from(this).areNotificationsEnabled().not()

private fun Context.isPermissionNotGranted(permission: String): Boolean =
    (ContextCompat.checkSelfPermission(
        this,
        permission
    ) != PackageManager.PERMISSION_GRANTED)

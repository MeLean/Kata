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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.milen.kata.R

class ForegroundServiceExperimentActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val notifyPermissionStr = Manifest.permission.POST_NOTIFICATIONS

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when (isGranted) {
                true -> startServiceAndFinish()
                else -> showNotificationPermissionAlert()
            }
        }

    private lateinit var startServiceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground_service_experiment)

        startServiceButton = findViewById<Button>(R.id.btn_start_service).also {
            it.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    isPermissionNotGranted(notifyPermissionStr)
                ) {
                    permissionLauncher.launch(notifyPermissionStr)
                } else {
                    startServiceAndFinish()
                }
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

    private fun startServiceAndFinish(): Unit =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && areNotificationsDisabled()) {
            showNotificationPermissionAlert()
        } else {
            startPermittedService()
        }

    private fun startPermittedService() {
        MyCountingService.startForeground(context = this).also {
            showToast(getString(R.string.foreground_service_started))
            finishAffinity()
        }
    }
}

private fun Context.showToast(msg: String): Unit =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

@RequiresApi(Build.VERSION_CODES.O)
private fun Context.areNotificationsDisabled(): Boolean =
    NotificationManagerCompat.from(this).areNotificationsEnabled().not()


private fun Context.showAlert(
    title: String = getString(android.R.string.dialog_alert_title),
    msg: String,
    onOkClicked: () -> Unit,
    onCancelClicked: () -> Unit
): Unit = AlertDialog.Builder(this)
    .apply {
        setTitle(title)
        setMessage(msg)
        setIcon(R.drawable.ic_attention)
        setPositiveButton(getString(android.R.string.ok)) { dialog, _ ->
            onOkClicked()
            dialog.dismiss()
        }
        setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
            onCancelClicked()
            dialog.dismiss()
        }
    }
    .create()
    .show()

private fun Context.isPermissionNotGranted(permission: String): Boolean =
    (ContextCompat.checkSelfPermission(
        this,
        permission
    ) != PackageManager.PERMISSION_GRANTED)

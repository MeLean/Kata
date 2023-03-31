package com.milen.kata.utils

import android.app.ActivityManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.milen.kata.R


fun Context.showAlert(
    msg: String,
    @DrawableRes iconRes: Int = R.drawable.ic_attention,
    title: String = getString(android.R.string.dialog_alert_title),
    onOkClicked: () -> Unit = {},
    onCancelClicked: (() -> Unit)? = null
): Unit = AlertDialog.Builder(this)
    .apply {
        setTitle(title)
        setMessage(msg)
        setIcon(iconRes)
        setPositiveButton(getString(android.R.string.ok)) { dialog, _ ->
            onOkClicked()
            dialog.dismiss()
        }
        onCancelClicked?.let {
            setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                it.invoke()
                dialog.dismiss()
            }
        }
    }
    .create()
    .show()


fun Context.isServiceRunning(serviceClassName: String): Boolean =
    with(getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager) {
        getRunningServices(Integer.MAX_VALUE).any { service ->
            service.foreground && service.service.className == serviceClassName
        }
    }
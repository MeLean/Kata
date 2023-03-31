package com.milen.kata.experiments.foregroundservice.services

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import com.milen.kata.experiments.foregroundservice.UNKNOWN
import com.milen.kata.experiments.foregroundservice.WIFI

@SuppressLint("MissingPermission")
class MeasurementManager {

    fun getNetworkSpeedType(context: Context): String =
        with(context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager) {
            when (networkType) {
                TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
                TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
                TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
                TelephonyManager.NETWORK_TYPE_EHRPD -> "eHRPD"
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO rev. 0"
                TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO rev. A"
                TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO rev. B"
                TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
                TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
                TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
                TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPA+"
                TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
                TelephonyManager.NETWORK_TYPE_IDEN -> "iDen"
                TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
                TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
                TelephonyManager.NETWORK_TYPE_NR -> "NR"
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> "Unknown"
                else -> "New type"
            }
        }

    fun getNetworkType(context: Context): String {
        val result: String
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        if (connectivityManager != null) {
            val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            result = if (wifi != null && wifi.isConnectedOrConnecting) {
                WIFI
            } else if (mobile != null && mobile.isConnectedOrConnecting) {
                getMobileNetworkType(context)
            } else {
                UNKNOWN
            }
        } else {
            result = getMobileNetworkType(context)
        }
        return result
    }


    private fun getMobileNetworkType(context: Context): String =
        with(context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager) {
            when (this?.networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN -> TWO_G
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP -> THREE_G
                TelephonyManager.NETWORK_TYPE_LTE -> FOUR_G
                TelephonyManager.NETWORK_TYPE_NR -> FIVE_G
                else -> UNKNOWN
            }
        }


    companion object {
        private const val TWO_G = "TWO_G"
        private const val THREE_G = "THREE_G"
        private const val FOUR_G = "FOUR_G"
        private const val FIVE_G = "FIVE_G"
    }
}
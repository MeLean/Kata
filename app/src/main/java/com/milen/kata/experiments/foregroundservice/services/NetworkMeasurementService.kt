package com.milen.kata.experiments.foregroundservice.services

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.*
import android.telephony.ServiceState.*
import android.telephony.gsm.GsmCellLocation
import com.milen.kata.experiments.foregroundservice.UNKNOWN
import com.milen.kata.experiments.foregroundservice.WIFI


@SuppressLint("MissingPermission")
class NetworkMeasurementService(
    private val telephonyManager: TelephonyManager,
    private val connectivityManager: ConnectivityManager?
) {

    fun getCsisinratio(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            (cellInfo.cellSignalStrength as CellSignalStrengthNr).csiSinr.toString()
        } else {
            UNKNOWN
        }
    }

    fun getSssinr(): String {
        val ssRsrq = getSsrsrq().toIntOrNull()
        val ssRsrp = getSsrsrp().toIntOrNull()

        return if (ssRsrp != null && ssRsrq != null) {
            (ssRsrp - ssRsrq).toString()
        } else {
            UNKNOWN
        }
    }

    fun getNrrssi(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            (cellInfo.cellSignalStrength as CellSignalStrengthNr).ssSinr.toString()
        } else {
            UNKNOWN
        }
    }

    fun getCsirssi(): String = UNKNOWN //todo not found how to do it

    fun getCsirsrq(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            (cellInfo.cellSignalStrength as CellSignalStrengthNr).csiRsrq.toString()
        } else {
            UNKNOWN
        }
    }

    fun getCsirsrp(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            (cellInfo.cellSignalStrength as CellSignalStrengthNr).csiRsrp.toString()
        } else {
            UNKNOWN
        }
    }

    fun getSsrsrq(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            (cellInfo.cellSignalStrength as CellSignalStrengthNr).ssRsrq.toString()
        } else if (cellInfo is CellInfoLte) {
            cellInfo.cellSignalStrength.rsrq.toString()
        } else {
            UNKNOWN
        }
    }

    fun getSsrsrp(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            (cellInfo.cellSignalStrength as CellSignalStrengthNr).ssRsrp.toString()
        } else if (cellInfo is CellInfoLte) {
            cellInfo.cellSignalStrength.rsrp.toString()
        } else {
            UNKNOWN
        }
    }

    fun getDl(): String =
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.linkDownstreamBandwidthKbps?.toString() ?: UNKNOWN

    fun getUl(): String =
        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.linkUpstreamBandwidthKbps?.toString() ?: UNKNOWN

    fun getTabn(): String =
        telephonyManager.allCellInfo.filterIsInstance<CellInfoLte>()
            .firstOrNull()?.cellSignalStrength?.timingAdvance?.toString() ?: UNKNOWN

    fun getCqi(): String =
        telephonyManager.allCellInfo.filterIsInstance<CellInfoLte>()
            .firstOrNull()?.cellSignalStrength?.cqi?.toString() ?: UNKNOWN

    fun getSnr(): String =
        telephonyManager.allCellInfo.filterIsInstance<CellInfoLte>()
            .firstOrNull()?.cellSignalStrength?.rssnr?.toString() ?: UNKNOWN


    fun getRssi(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()

        return if (cellInfo is CellInfoLte && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            cellInfo.cellSignalStrength.rssi.toString()
        } else if (cellInfo is CellInfoWcdma) {
            cellInfo.cellSignalStrength.dbm.toString()
        } else if (cellInfo is CellInfoGsm) {
            cellInfo.cellSignalStrength.dbm.toString()
        } else {
            UNKNOWN
        }
    }

    fun getRsrq(): String {
        val cellInfo = telephonyManager.allCellInfo?.firstOrNull()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            ((cellInfo).cellSignalStrength as CellSignalStrengthNr).csiRsrq.toString()
        } else if (cellInfo is CellInfoLte) {
            cellInfo.cellSignalStrength.rsrq.toString()
        } else {
            UNKNOWN
        }
    }

    fun getRsrp(): String {
        val cellInfo = telephonyManager.allCellInfo.firstOrNull()

        return if (cellInfo is CellInfoLte) {
            cellInfo.cellSignalStrength.rsrp.toString()
        } else if (cellInfo is CellInfoWcdma) {
            cellInfo.cellSignalStrength.dbm.toString()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo is CellInfoNr) {
            ((cellInfo).cellSignalStrength as CellSignalStrengthNr).csiRsrp.toString()
        } else {
            UNKNOWN
        }
    }

    fun getSessionManagementState(): String = when (telephonyManager.serviceState?.state) {
        ServiceState.STATE_IN_SERVICE -> STATE_IN_SERVICE
        ServiceState.STATE_OUT_OF_SERVICE -> STATE_OUT_OF_SERVICE
        ServiceState.STATE_EMERGENCY_ONLY -> STATE_EMERGENCY_ONLY
        ServiceState.STATE_POWER_OFF -> STATE_POWER_OFF
        else -> UNKNOWN
    }

    fun isEndcAvailable(): Boolean =
        telephonyManager.allCellInfo.firstOrNull()?.let {
            isEndcAvailableForCell(it)
        } ?: false

    fun getBandIdentifier(): String =
        (telephonyManager.allCellInfo.firstOrNull() as? CellInfoLte)?.cellIdentity?.earfcn
            ?.let {
                getLteBand(it).toString()
            } ?: UNKNOWN

    fun getCurrentBandwidth(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (telephonyManager.allCellInfo.find { it is CellInfoLte } as? CellInfoLte)?.cellIdentity
                ?.bandwidth?.toString() ?: UNKNOWN
        } else {
            UNKNOWN //todo check for SDK lower than 28 it is ok to return UNKNOWN
        }

    // todo get more info about that
    fun getBWs(): String = getCurrentBandwidth().toIntOrNull()
        ?.let {
            (it / 1000).toString()
        } ?: UNKNOWN

    // todo thick to concatenate in the model building
    fun getRfcn(): String {
        return when (val cellInfo = telephonyManager.allCellInfo?.firstOrNull()) {
            is CellInfoGsm -> {
                (cellInfo.cellIdentity.arfcn - 10).toString()
            }
            is CellInfoWcdma -> {
                ((cellInfo.cellIdentity.uarfcn - 9612) / 5).toString()
            }
            is CellInfoLte -> {
                (cellInfo.cellIdentity.earfcn - 18000).toString()
            }
            else -> UNKNOWN
        }
    }

    fun getFrequency(): String =
        (telephonyManager.allCellInfo.find { it is CellInfoLte } as? CellInfoLte)?.cellIdentity?.earfcn
            ?.toString() ?: UNKNOWN

    fun getPci(): String =
        (telephonyManager.allCellInfo.find { it is CellInfoLte } as? CellInfoLte)?.cellIdentity?.pci
            ?.toString() ?: UNKNOWN

    fun getTac(): String =
        (telephonyManager.allCellInfo.find { it is CellInfoLte } as? CellInfoLte)?.cellIdentity?.tac
            ?.toString() ?: UNKNOWN

    // todo thick to concatenate in the model building
    fun getPlmn(): String = "${getRoam()}${getMcc()}${getMnc()}"

    fun getLcid(): String =
        (telephonyManager.cellLocation as? GsmCellLocation)?.let {
            it.lac.toString()
        } ?: UNKNOWN

    fun getEci(): String =
        (telephonyManager.allCellInfo?.firstOrNull() as? CellInfoLte)?.let {
            ((it.cellIdentity.tac and 0xffff) shl 16 or (it.cellIdentity.ci and 0xffff)).toString()
        } ?: UNKNOWN

    fun getNodeBId(): String =
        (telephonyManager.allCellInfo.find { it is CellInfoLte } as? CellInfoLte)?.cellIdentity?.ci?.toString()
            ?: UNKNOWN

    fun getRoam(): String =
        if (telephonyManager.isNetworkRoaming) ROAMING else HOME


    fun getNetworkOperatorName(): String = telephonyManager.networkOperatorName

    fun getMcc(): String =
        telephonyManager.networkOperator.takeIf { it.length > 3 }?.substring(0, 3) ?: UNKNOWN

    fun getMnc(): String =
        telephonyManager.networkOperator.takeIf { it.length > 3 }?.substring(3) ?: UNKNOWN

    fun getNetworkSpeedType(): String =
        with(telephonyManager) {
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

    fun getNetworkType(): String =
        connectivityManager?.let {
            val wifi = it.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = it.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (wifi != null && wifi.isConnectedOrConnecting) {
                WIFI
            } else if (mobile != null && mobile.isConnectedOrConnecting) {
                getMobileNetworkType()
            } else {
                UNKNOWN
            }
        } ?: getMobileNetworkType()


    private fun getMobileNetworkType(): String =
        with(telephonyManager) {
            when (networkType) {
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

    private fun isEndcAvailableForCell(cellInfo: CellInfo): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (cellInfo is CellInfoNr) {
                try {
                    val isEndcAvailableMethod =
                        CellInfoNr::class.java.getDeclaredMethod("isEndcAvailable")
                    return isEndcAvailableMethod.invoke(cellInfo) as? Boolean ?: false
                } catch (e: NoSuchMethodException) {
                    // isEndcAvailable method doesn't exist
                }
            }
        }
        return false
    }

    private fun getLteBand(earfcn: Int): Int {
        return when (earfcn) {
            in 1..599 -> 1
            in 600..1199 -> 2
            in 1200..1949 -> 3
            in 1950..2399 -> 4
            in 2400..2649 -> 5
            in 2650..2749 -> 6
            in 2750..3449 -> 7
            in 3450..3799 -> 8
            in 3800..4149 -> 9
            in 4150..4749 -> 10
            in 4750..4949 -> 11
            in 5010..5179 -> 12
            in 5180..5279 -> 13
            in 5280..5379 -> 14
            in 5730..5849 -> 17
            in 5850..5999 -> 18
            in 6000..6149 -> 19
            in 6150..6449 -> 20
            in 6450..6599 -> 21
            in 6600..7399 -> 22
            in 7500..7699 -> 24
            in 7700..8039 -> 25
            in 8040..8689 -> 26
            in 8690..9039 -> 27
            in 9040..9209 -> 28
            in 9210..9659 -> 29
            in 9660..9769 -> 30
            in 9770..9869 -> 31
            in 9870..9919 -> 32
            in 9920..10359 -> 33
            in 36000..36199 -> 34
            in 36200..36349 -> 35
            in 36350..36949 -> 36
            in 36950..37549 -> 37
            in 37550..37749 -> 38
            in 37750..38249 -> 39
            in 38250..38649 -> 40
            in 38650..39649 -> 41
            in 39650..41589 -> 42
            in 41590..43589 -> 43
            in 43590..45589 -> 44
            in 45590..46589 -> 45
            in 46590..46789 -> 46
            in 46790..54539 -> 47
            in 54540..55239 -> 48
            in 231000..231799 -> 65
            in 386600..396599 -> 66
            in 396600..415899 -> 67
            else -> -1
        }
    }

    companion object {
        private const val TWO_G = "GSM"
        private const val THREE_G = "3G"
        private const val FOUR_G = "4G"
        private const val FIVE_G = "5G"
        private const val HOME = "home"
        private const val ROAMING = "roaming"
        private const val STATE_IN_SERVICE = "IN_SERVICE"
        private const val STATE_OUT_OF_SERVICE = "OUT_OF_SERVICE"
        private const val STATE_EMERGENCY_ONLY = "EMERGENCY_ONLY"
        private const val STATE_POWER_OFF = "POWER_OFF"

    }
}
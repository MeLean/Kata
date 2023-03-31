package com.milen.kata.utils

import android.util.Log
import com.milen.kata.BuildConfig

object DebugLogger {
    fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("TEST_IT", msg)
        }
    }
}
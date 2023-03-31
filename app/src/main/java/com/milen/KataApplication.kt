package com.milen

import android.app.Application
import com.milen.kata.experiments.fakeserver.server.FakeServer
import com.milen.kata.experiments.fakeserver.server.mockeddata.getMockedOkData
import com.milen.kata.utils.DebugLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class KataApplication : Application() {
    private val fakeServer: FakeServer by lazy {
        runBlocking(Dispatchers.Default) {
            val server = FakeServer().apply {
                setResponses(getMockedOkData())
            }
            while (server.isReady.not()) {
                delay(20)
            }
            server
        }
    }

    override fun onTrimMemory(level: Int) {
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            DebugLogger.log("${applicationContext.applicationInfo.className}.onTrimMemory level: $level")
            fakeServer.shutdown()
        }
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
        DebugLogger.log("${applicationContext.applicationInfo.className}.onTerminate called!")
        fakeServer.shutdown()
    }
}
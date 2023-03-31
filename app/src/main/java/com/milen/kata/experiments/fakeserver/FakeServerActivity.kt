package com.milen.kata.experiments.fakeserver

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.milen.kata.R
import com.milen.kata.experiments.fakeserver.api.data.ApiResponse
import com.milen.kata.experiments.fakeserver.api.repository.MyRepository
import com.milen.kata.experiments.fakeserver.api.repository.MyRepositoryImpl
import com.milen.kata.utils.showAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FakeServerActivity : AppCompatActivity() {
    //Not good practice just for sake of the experiment
    private val repo: MyRepository = MyRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fake_server)

        initCLickListeners()
    }

    private fun initCLickListeners() {
        findViewById<Button>(R.id.make_success_request).setOnClickListener {
            startSuccessRequest()
        }

        findViewById<Button>(R.id.make_success_request_v2).setOnClickListener {
            startSuccessV2Request()
        }

        findViewById<Button>(R.id.make_request_error).setOnClickListener {
            startErrorRequest()
        }
    }

    private fun startSuccessRequest(): Unit =
        runInCoroutineScopeWithHandling { repo.getSuccessData() }

    private fun startSuccessV2Request(): Unit =
        runInCoroutineScopeWithHandling { repo.getSuccessV2Data() }

    private fun startErrorRequest(): Unit =
        runInCoroutineScopeWithHandling { repo.getErrorData() }

    private fun runInCoroutineScopeWithHandling(block: suspend () -> ApiResponse) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val response = block()
                withContext(Dispatchers.Main) {
                    showAlert(
                        iconRes = R.drawable.ic_update,
                        title = "Success",
                        msg = response.message
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showAlert(
                        title = "Error",
                        msg = e.message.orEmpty()
                    )
                }
            }
        }
    }
}
package com.milen.kata.experiments.fakeserver.server

import com.milen.kata.experiments.fakeserver.server.mockeddata.MockResponseModel
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection

class FakeServer(private val port: Int = DEFAULT_PORT) {
    private val mockWebServer = MockWebServer()
    var isReady: Boolean = false
        private set

    init {
        mockWebServer.start(port = port)
    }
    
    fun setResponses(responses: Map<String, MockResponseModel>) {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse =
                when {
                    responses.keys.contains(request.path) ->
                        responses[request.path]?.let {
                            MockResponse()
                                .setResponseCode(it.code)
                                .setBody(it.json)
                        } ?: MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)

                    else -> MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                }
        }

        isReady = true
    }

    fun shutdown() {
        mockWebServer.shutdown()
    }

    fun getBaseUrl(): HttpUrl {
        return mockWebServer.url("/")
    }

    companion object {
        private const val DEFAULT_PORT = 8081
    }
}
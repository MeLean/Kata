package com.milen.kata.experiments.fakeserver.server.mockeddata

import com.google.gson.Gson
import com.milen.kata.experiments.fakeserver.api.data.ApiResponse
import java.net.HttpURLConnection

fun getMockedOkData(): Map<String, MockResponseModel> =
    with(Gson()) {
        mapOf(
            "/success" to
                    MockResponseModel(
                        code = HttpURLConnection.HTTP_OK,
                        json = toJson(ApiResponse("success"))
                    ),

            "/successV2" to
                    MockResponseModel(
                        code = HttpURLConnection.HTTP_OK,
                        json = toJson(ApiResponse("successV2"))
                    ),

            "/error" to
                    MockResponseModel(
                        code = HttpURLConnection.HTTP_FORBIDDEN,
                        json = toJson(ApiResponse("error"))
                    )
        )
    }





package com.milen.kata.experiments.fakeserver.api

import com.milen.kata.experiments.fakeserver.api.data.ApiResponse
import retrofit2.http.GET

interface MyApi {
    @GET("success")
    suspend fun getSuccessData(): ApiResponse


    @GET("successV2")
    suspend fun getSuccessV2Data(): ApiResponse

    @GET("error")
    suspend fun getErrorData(): ApiResponse
}
package com.milen.kata.experiments.fakeserver.api.repository

import com.milen.kata.BuildConfig
import com.milen.kata.experiments.fakeserver.api.MyApi
import com.milen.kata.experiments.fakeserver.api.data.ApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface MyRepository {
    suspend fun getSuccessData(): ApiResponse
    suspend fun getSuccessV2Data(): ApiResponse
    suspend fun getErrorData(): ApiResponse
}

class MyRepositoryImpl : MyRepository {
    private val client = OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val myApi: MyApi = retrofit.create(MyApi::class.java)

    override suspend fun getSuccessData(): ApiResponse = myApi.getSuccessData()
    override suspend fun getSuccessV2Data(): ApiResponse = myApi.getSuccessV2Data()
    override suspend fun getErrorData(): ApiResponse = myApi.getErrorData()
}
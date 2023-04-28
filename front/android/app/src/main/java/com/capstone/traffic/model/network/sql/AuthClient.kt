package com.capstone.traffic.model.network.sql

import com.capstone.traffic.global.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthClient {
    private var instance: Retrofit? = null

    fun getInstance(): Retrofit {
        if (instance == null) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
            // 로컬 환경 테스트
            // 10.0.2.2 -> 에뮬
            // 디바이스 -> 본인
            instance =  Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return instance!!
        }
        return instance!!
    }
}

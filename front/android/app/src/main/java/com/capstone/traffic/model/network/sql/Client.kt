package com.capstone.traffic.model.network.sql

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client{
    private var instance : Retrofit? = null

    fun getInstance() : Retrofit {
        if(instance == null)
        {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            // 로컬 환경 테스트
            instance =  Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/user/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return instance!!
    }
}
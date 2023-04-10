package com.capstone.traffic.model.network.sk.direction

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

            instance =  Retrofit.Builder()
                .baseUrl("https://apis.openapi.sk.com/transit/routes/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return instance!!
    }
}
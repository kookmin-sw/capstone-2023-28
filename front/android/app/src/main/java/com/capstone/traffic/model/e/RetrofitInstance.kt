package com.capstone.traffic.model.e

import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sql.Client
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitInstance {
    val BASE_URL = "http://10.0.2.2:8000/"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(Client.HeaderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()


    val client = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance(): Retrofit{
        return client
    }

    class HeaderInterceptor constructor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = "Bearer ${MyApplication.prefs.getToken()}"
            val newRequest = chain.request().newBuilder().addHeader("Authorization", token).build()
            return chain.proceed(newRequest)
        }
    }
}
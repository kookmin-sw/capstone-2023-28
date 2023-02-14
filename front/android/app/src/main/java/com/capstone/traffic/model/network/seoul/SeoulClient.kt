package com.capstone.traffic.model.network.seoul
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SeoulClient{
    private var instance : Retrofit? = null

    fun getInstance() : Retrofit {
        if(instance == null)
        {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            instance =  Retrofit.Builder()
                .baseUrl("http://swopenAPI.seoul.go.kr/api/subway/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return instance!!
    }
}
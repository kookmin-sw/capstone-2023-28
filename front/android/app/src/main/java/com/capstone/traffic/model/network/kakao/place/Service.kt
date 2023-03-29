package com.capstone.traffic.model.network.kakao.place

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface Service {
    @GET("keyword.json")
    fun getResponse( @Header("Authorization") Authorization : String , @Query("page") page : String, @Query("size") size : String, @Query("sort") sort:String, @Query("query") query : String): Call<Response>
}


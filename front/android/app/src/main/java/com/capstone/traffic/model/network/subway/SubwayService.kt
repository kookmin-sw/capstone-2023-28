package com.capstone.traffic.model.network.subway

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SubwayService {
    @GET("subwayAPI?")
    fun getResponse(@Query("line") line:String): Call<Response>
}
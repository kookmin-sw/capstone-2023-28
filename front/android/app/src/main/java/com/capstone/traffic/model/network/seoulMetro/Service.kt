package com.capstone.traffic.model.network.seoulMetro

import retrofit2.Call
import retrofit2.http.GET

interface Service {
    @GET("twitter-metro-tweets")
    fun getTwit() : Call<Res>
}
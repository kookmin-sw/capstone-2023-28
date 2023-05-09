package com.capstone.traffic.model.network.demon

import retrofit2.Call
import retrofit2.http.GET

interface DemonService {
    @GET("cloudwatch-police-json")
    fun getResponse(): Call<Response>
}
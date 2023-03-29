package com.capstone.traffic.model.network.demon

import retrofit2.Call
import retrofit2.http.GET

interface DemonService {
    @GET("police-s3-read")
    fun getResponse(): Call<Response>
}
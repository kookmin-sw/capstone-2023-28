package com.capstone.traffic.model.network.twitter

import retrofit2.Call
import retrofit2.http.GET

interface SubwayService {
    @GET()
    fun getResponse(): Call<ResponseData>
}
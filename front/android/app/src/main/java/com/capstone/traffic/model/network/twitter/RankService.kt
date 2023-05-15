package com.capstone.traffic.model.network.twitter

import retrofit2.Call
import retrofit2.http.GET

interface RankService {
    @GET("twitter-line-ranking")
    fun getResponse(): Call<ResponseData>
}
package com.capstone.traffic.model.network.news

import retrofit2.Call
import retrofit2.http.GET

interface Service {
    @GET("naver-api-news")
    fun getNews() : Call<Res>
}
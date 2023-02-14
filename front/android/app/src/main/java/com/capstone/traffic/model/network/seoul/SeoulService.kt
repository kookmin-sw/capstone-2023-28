package com.capstone.traffic.model.network.seoul

import com.capstone.traffic.global.appkey.APIKEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface SeoulService {
    @GET("${APIKEY.SEOUL_APIKEY}}/json/realtimePosition/0/1000/2{subwayNm}")
    fun getResponse(@Path("subwayNm", encoded = true) subwayNm: String): Call<Seoul>

    @GET
    fun getInfo(@Url url : String) : Call<Seoul>
}
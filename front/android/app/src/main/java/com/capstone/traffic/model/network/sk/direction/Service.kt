package com.capstone.traffic.model.network.sk.direction

import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.model.network.sk.direction.dataClass.objects
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Service {
    @POST(".")
    fun getDirection(@Header("accept") accept: String = "application/json", @Header("content-type") content: String = "application/json", @Header("appKey") appKey: String = APIKEY.SK_APIKEY, @Body param: RequestBody) : Call<objects>
}
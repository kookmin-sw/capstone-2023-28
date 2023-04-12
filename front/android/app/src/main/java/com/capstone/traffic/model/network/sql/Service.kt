package com.capstone.traffic.model.network.sql

import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import com.capstone.traffic.model.network.sql.dataclass.login.LoginResSuc
import com.capstone.traffic.model.network.sql.dataclass.sign.SignResSuc
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Service {
    @POST("signup/")
    fun getSign(@Body param: RequestBody) : Call<SignResSuc>

    @POST("token/")
    fun getLogin(@Body param: RequestBody) : Call<LoginResSuc>

    @GET("info/")
    fun getInfo(@Query("user_email") userEmail : String) : Call<InfoRecSuc>
}
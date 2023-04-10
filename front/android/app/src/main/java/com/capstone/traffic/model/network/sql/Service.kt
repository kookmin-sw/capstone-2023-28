package com.capstone.traffic.model.network.sql

import com.capstone.traffic.model.network.sql.dataclass.login.LoginResSuc
import com.capstone.traffic.model.network.sql.dataclass.sign.SignResSuc
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {
    @POST("signup/")
    fun getSign(@Body param: RequestBody) : Call<SignResSuc>

    @POST("token/")
    fun getLogin(@Body param: RequestBody) : Call<LoginResSuc>
}
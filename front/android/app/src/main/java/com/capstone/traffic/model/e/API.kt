package com.capstone.traffic.model.e

import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import okhttp3.Interceptor
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface API {
    @Multipart
    @POST("feed/image/")
    fun sendImage(
        @Part("feed_id") userId: String,
        @Part image: MultipartBody.Part
    ): Call<ImageUpload>

}
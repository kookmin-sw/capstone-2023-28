package com.capstone.traffic.model.network.seoul

import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("status") val status : String,
    @SerializedName("code") val code : String,
    @SerializedName("message") val message: String
)
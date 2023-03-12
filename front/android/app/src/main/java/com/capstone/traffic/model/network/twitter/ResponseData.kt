package com.capstone.traffic.model.network.twitter

import com.google.gson.annotations.SerializedName

data class ResponseData(
    @SerializedName("data") val data : List<Data>
)
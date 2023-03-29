package com.capstone.traffic.model.network.subway

import com.google.gson.annotations.SerializedName

data class ResponseList(
    @SerializedName("line") val line : String,
    @SerializedName("express") val express : String,
    @SerializedName("value") val value : List<Value>,
)
package com.capstone.traffic.model.network.subway

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("response") val response : List<ResponseList>
)
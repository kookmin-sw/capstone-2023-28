package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class requestParameters(
    @SerializedName("busCount") val busCount : String,
    @SerializedName("subwayCount") val subwayCount : String,
    @SerializedName("subwayBusCount") val subwayBusCount : String
)

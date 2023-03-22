package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class metaData(
    @SerializedName("requestParameters") val requestParameters : requestParameters,
    @SerializedName("plan") val plan : plan
)

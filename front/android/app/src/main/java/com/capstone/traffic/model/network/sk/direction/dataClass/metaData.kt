package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class metaData(
    @SerializedName("requestParameters") val requestParameters : List<requestParameters>,
    @SerializedName("plan") val plan : List<plan>
)

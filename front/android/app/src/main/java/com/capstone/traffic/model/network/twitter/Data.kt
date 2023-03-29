package com.capstone.traffic.model.network.twitter

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("name") val name : String,
    @SerializedName("rank") val rank : String,
    @SerializedName("count") val count : String
)
package com.capstone.traffic.model.network.seoulMetro

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("tweet") val tweet : String,
    @SerializedName("info") val info : Info,

)

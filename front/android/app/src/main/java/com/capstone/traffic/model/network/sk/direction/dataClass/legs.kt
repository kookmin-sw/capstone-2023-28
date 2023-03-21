package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class legs(
    @SerializedName("mode") val mode : String,
    @SerializedName("sectionTime") val sectionTime : String,
    @SerializedName("start") val start : List<name>,
    @SerializedName("end") val end : List<name>,
    @SerializedName("route") val route : String
)

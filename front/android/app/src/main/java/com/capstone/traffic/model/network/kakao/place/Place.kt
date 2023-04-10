package com.capstone.traffic.model.network.kakao.place

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("place_name") val place_name : String,
    @SerializedName("x") val x : String,
    @SerializedName("y") val y : String,
)
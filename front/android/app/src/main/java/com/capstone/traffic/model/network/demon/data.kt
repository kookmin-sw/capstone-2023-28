package com.capstone.traffic.model.network.demon

import com.capstone.traffic.model.network.kakao.place.Place
import com.google.gson.annotations.SerializedName

data class data(
    @SerializedName("data") val data : String,
    @SerializedName("protest") val protest : Place
)

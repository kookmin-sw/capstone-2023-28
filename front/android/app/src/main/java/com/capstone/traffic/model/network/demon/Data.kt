package com.capstone.traffic.model.network.demon

import com.capstone.traffic.model.network.kakao.place.Place
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("date") val date : String,
    @SerializedName("protest") val protest : Protest
)

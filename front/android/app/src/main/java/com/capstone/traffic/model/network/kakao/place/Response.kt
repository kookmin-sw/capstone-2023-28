package com.capstone.traffic.model.network.kakao.place

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("documents") val documents : List<Place>,
)

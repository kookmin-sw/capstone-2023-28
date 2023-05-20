package com.capstone.traffic.model.network.seoulMetro

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("data") val data : List<Data>
)
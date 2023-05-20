package com.capstone.traffic.model.network.news

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("data") val data : List<News>,
)
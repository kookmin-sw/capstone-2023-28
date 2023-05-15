package com.capstone.traffic.model.network.news

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("news") val news : String,
    @SerializedName("url") val url : String,

)

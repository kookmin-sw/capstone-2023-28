package com.capstone.traffic.model.network.news

import com.google.gson.annotations.SerializedName

data class NewSet(
    @SerializedName("image") val image : String?,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("url") val url : String?
)

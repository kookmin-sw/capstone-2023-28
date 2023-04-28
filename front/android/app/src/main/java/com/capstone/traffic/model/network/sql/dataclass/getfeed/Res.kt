package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("feed_id") val feedId : String,
    @SerializedName("user_id") val userId : String,
    @SerializedName("content") val content : String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("comments") val comments: List<Comments>?,
    @SerializedName("images") val images: List<Images>?

)

package com.capstone.traffic.model.network.sql.dataclass.comment

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("feed_id") val feedId : String,
    @SerializedName("content") val content : String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("user") val user : User
)

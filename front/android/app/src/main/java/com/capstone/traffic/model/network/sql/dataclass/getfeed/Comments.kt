package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class Comments(
    @SerializedName("content") val content : String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String
)

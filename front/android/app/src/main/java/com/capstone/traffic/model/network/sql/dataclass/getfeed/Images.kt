package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("id") val id : String,
    @SerializedName("image") val image : String
)

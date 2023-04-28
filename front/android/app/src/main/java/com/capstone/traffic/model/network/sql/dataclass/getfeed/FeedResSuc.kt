package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class FeedResSuc(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : List<Res>,
)

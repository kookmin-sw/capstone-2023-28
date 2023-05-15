package com.capstone.traffic.model.network.sql.dataclass.postfeed

import com.google.gson.annotations.SerializedName

data class response (
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : res
    )
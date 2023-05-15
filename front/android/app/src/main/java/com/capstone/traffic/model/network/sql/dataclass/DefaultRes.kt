package com.capstone.traffic.model.network.sql.dataclass

import com.google.gson.annotations.SerializedName

data class DefaultRes(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : ErrorRes?
)

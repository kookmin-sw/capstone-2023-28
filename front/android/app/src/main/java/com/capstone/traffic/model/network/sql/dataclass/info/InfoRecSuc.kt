package com.capstone.traffic.model.network.sql.dataclass.info

import com.google.gson.annotations.SerializedName

data class InfoRecSuc(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : Res
)

package com.capstone.traffic.model.network.sql.dataclass.comment

import com.google.gson.annotations.SerializedName

data class ComResSuc(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : Res
)
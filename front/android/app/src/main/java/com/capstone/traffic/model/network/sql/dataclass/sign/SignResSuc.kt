package com.capstone.traffic.model.network.sql.dataclass.sign

import com.google.gson.annotations.SerializedName

data class SignResSuc(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : Res
)
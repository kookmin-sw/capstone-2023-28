package com.capstone.traffic.model.network.sql.dataclass.login

import com.google.gson.annotations.SerializedName

data class LoginResSuc(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : Res
)
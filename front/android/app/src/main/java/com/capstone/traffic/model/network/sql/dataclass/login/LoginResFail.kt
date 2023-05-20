package com.capstone.traffic.model.network.sql.dataclass.login

import com.google.gson.annotations.SerializedName

data class LoginResFail(
    @SerializedName("status") val status : String,
    @SerializedName("res") val res : ErrorRes
)

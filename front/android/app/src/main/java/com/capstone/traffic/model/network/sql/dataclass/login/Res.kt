package com.capstone.traffic.model.network.sql.dataclass.login

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("refresh") val refresh : String,
    @SerializedName("access") val access : String,
)


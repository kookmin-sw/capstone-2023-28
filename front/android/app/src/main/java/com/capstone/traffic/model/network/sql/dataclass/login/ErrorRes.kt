package com.capstone.traffic.model.network.sql.dataclass.login

import com.google.gson.annotations.SerializedName

data class ErrorRes(
    @SerializedName("error_name") val errorName : String,
    @SerializedName("error_id") val errorId : String,
)
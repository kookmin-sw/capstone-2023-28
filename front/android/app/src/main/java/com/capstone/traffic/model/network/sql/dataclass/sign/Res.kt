package com.capstone.traffic.model.network.sql.dataclass.sign

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("error_name") val errorName : String,
    @SerializedName("error_id") val errorId : String,
)

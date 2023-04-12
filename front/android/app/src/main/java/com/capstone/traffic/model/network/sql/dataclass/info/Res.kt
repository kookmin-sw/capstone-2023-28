package com.capstone.traffic.model.network.sql.dataclass.info

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("user_email") val userEmail : String,
    @SerializedName("user_nickname") val userNickName : String,
    @SerializedName("user_definition") val userDefinition : String,
    @SerializedName("user_point_number") val userPointNumber : String,
    @SerializedName("user_profile_image") val user_profile_image : String
)

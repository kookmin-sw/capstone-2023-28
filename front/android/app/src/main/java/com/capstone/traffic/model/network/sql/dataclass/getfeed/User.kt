package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_nickname") val userNickname : String?,
    @SerializedName("user_email") val userEmail : String,
    @SerializedName("user_definition") val userDefinition : String,
    @SerializedName("user_profile_image") val userProfile : String?,
)

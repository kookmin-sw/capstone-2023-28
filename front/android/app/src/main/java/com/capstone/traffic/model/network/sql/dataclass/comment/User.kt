package com.capstone.traffic.model.network.sql.dataclass.comment

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId : String,
    @SerializedName("user_email") val userEmail : String,
    @SerializedName("user_profile_image") val userProfileImage : String?,
    @SerializedName("user_nickname") val userNickname : String,
)

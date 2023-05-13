package com.capstone.traffic.model.network.sql.dataclass.info

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("user_email") val userEmail : String,
    @SerializedName("user_nickname") val userNickName : String,
    @SerializedName("user_definition") val userDefinition : String,
    @SerializedName("user_profile_image") val user_profile_image : String?,
    @SerializedName("follower_num") val followerNum : String,
    @SerializedName("following_num") val followingNum : String,
    @SerializedName("is_follower") val isFollower : String,
    @SerializedName("id") val id : String
)

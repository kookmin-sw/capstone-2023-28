package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class Res(
    @SerializedName("feed_id") val feedId : String,
    @SerializedName("user_id") val userId : String,
    @SerializedName("content") val content : String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String,
    @SerializedName("comments_num") val comments: String,
    @SerializedName("images") val images: List<Images>?,
    @SerializedName("user") val user: User?,
    @SerializedName("hash_tags") val hashTags: List<HashTags>?,
    @SerializedName("likes_num") val likesNum : String,
    @SerializedName("is_liked") val isLiked : String,
)

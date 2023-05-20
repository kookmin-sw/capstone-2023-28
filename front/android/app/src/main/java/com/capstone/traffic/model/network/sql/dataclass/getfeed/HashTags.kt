package com.capstone.traffic.model.network.sql.dataclass.getfeed

import com.google.gson.annotations.SerializedName

data class HashTags(
    @SerializedName("hash_tag") val hashTag : String,
)

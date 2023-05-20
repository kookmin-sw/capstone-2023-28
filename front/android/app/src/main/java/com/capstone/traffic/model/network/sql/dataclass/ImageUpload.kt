package com.capstone.traffic.model.network.sql.dataclass

import com.google.gson.annotations.SerializedName

data class ImageUpload(
    @SerializedName("status") val status : String,
)
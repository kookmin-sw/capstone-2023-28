package com.capstone.traffic.model.network.demon

import com.google.gson.annotations.SerializedName

data class place(
    @SerializedName("place") val place : String?,
    @SerializedName("region") val region : String?,
    @SerializedName("time") val time : String?,
    @SerializedName("people") val people : String?
)

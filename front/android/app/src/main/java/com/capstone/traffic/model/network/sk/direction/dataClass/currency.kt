package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class currency(
    @SerializedName("symbol") val symbol : String,
    @SerializedName("currency") val currency : String
)

package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class regular(
    @SerializedName("totalFare") val totalFare : String,
    @SerializedName("currency") val currency : List<currency>
)

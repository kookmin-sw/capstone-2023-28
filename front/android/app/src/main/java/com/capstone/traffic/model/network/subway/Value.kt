package com.capstone.traffic.model.network.subway

import com.google.gson.annotations.SerializedName

data class Value(
    @SerializedName("name") val name : String,
    @SerializedName("status") val status : List<Int>
)
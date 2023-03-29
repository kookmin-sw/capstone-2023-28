package com.capstone.traffic.model.network.demon

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("data") val Data : List<Data>
)

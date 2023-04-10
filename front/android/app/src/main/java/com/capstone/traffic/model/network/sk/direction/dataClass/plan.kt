package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class plan(
    @SerializedName("itineraries") val itineraries : List<itineraries>
)

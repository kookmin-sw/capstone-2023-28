package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class itineraries(
    @SerializedName("fare") val fare : List<fare>,
    @SerializedName("totalTime") val totalTime : String,
    @SerializedName("legs") val legs : List<legs>
)
package com.capstone.traffic.model.network.sk.direction.dataClass

import com.google.gson.annotations.SerializedName

data class fare(
    @SerializedName("regular") val regular : List<regular>
)

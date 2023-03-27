package com.capstone.traffic.model.network.demon

import com.google.gson.annotations.SerializedName

data class data(
    @SerializedName("date") val date : String,
    @SerializedName("protest") val protest : place
)

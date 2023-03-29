package com.capstone.traffic.model.network.seoul.arrive

import com.capstone.traffic.model.network.seoul.ErrorBody
import com.google.gson.annotations.SerializedName

data class Seoul(
    @SerializedName("errorMessage") val errorBody : ErrorBody,
    @SerializedName("realtimeArrivalList") val realtimeArrivalList : List<RealtimeArrivalList>
)
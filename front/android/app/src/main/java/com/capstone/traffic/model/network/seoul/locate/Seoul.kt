package com.capstone.traffic.model.network.seoul.locate

import com.capstone.traffic.model.network.seoul.ErrorBody
import com.google.gson.annotations.SerializedName

data class Seoul(
    @SerializedName("errorMessage") val errorBody : ErrorBody,
    @SerializedName("realtimePositionList") val realtimePositionList : List<RealtimePositionList>
)
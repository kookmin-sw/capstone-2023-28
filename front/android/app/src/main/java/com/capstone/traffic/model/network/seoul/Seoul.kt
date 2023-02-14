package com.capstone.traffic.model.network.seoul

import com.capstone.traffic.model.network.seoul.ErrorBody
import com.capstone.traffic.model.network.seoul.RealtimePositionList
import com.google.gson.annotations.SerializedName

data class Seoul(
    @SerializedName("errorMessage") val errorBody : ErrorBody,
    @SerializedName("realtimePositionList") val realtimePositionList : List<RealtimePositionList>
)
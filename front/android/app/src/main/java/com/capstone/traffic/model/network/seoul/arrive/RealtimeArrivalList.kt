package com.capstone.traffic.model.network.seoul.arrive

import com.google.gson.annotations.SerializedName

data class RealtimeArrivalList(
    @SerializedName("subwayId") val subwayId : String,
    @SerializedName("subwayHeading") val subwayHeading : String,
    @SerializedName("arvlMsg2") val arvlMsg2 : String,
    @SerializedName("arvlMsg3") val arvlMsg3 : String,
    @SerializedName("recptnDt") val recptnDt : String,
    @SerializedName("trainLineNm") val trainLineNm : String,
    @SerializedName("updnLine") val updnLine : String
)
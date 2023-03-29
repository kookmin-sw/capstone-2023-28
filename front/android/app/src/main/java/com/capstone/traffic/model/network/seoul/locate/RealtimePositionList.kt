package com.capstone.traffic.model.network.seoul.locate

import com.google.gson.annotations.SerializedName

data class RealtimePositionList(
    @SerializedName("statnNm") val statnNm : String,
    @SerializedName("statnId") val statnId : String,
    @SerializedName("trainNo") val trainNo : String,
    @SerializedName("updnLine") val updnLine : String,
    @SerializedName("statnTid") val statnTid : String,
    @SerializedName("statnTnm") val statnTnm : String,
    @SerializedName("trainSttus") val trainSttus : String,
    @SerializedName("directAt") val directAt : String,
    @SerializedName("lstcarAt") val lstcarAt : String
)

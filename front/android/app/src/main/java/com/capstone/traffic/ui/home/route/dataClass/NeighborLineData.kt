package com.capstone.traffic.ui.home.route.dataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NeighborLineData(
    val line : String,
    var left : String? = null,
    val center : String,
    var right : String? = null
) : Parcelable

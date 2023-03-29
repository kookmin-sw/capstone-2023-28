package com.capstone.traffic.ui.home.direction.transportType

import com.capstone.traffic.ui.home.direction.transportType.route.Route

data class DirectionData(
    val time : String,
    val price : String,
    var route : List<Route>
)
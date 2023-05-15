package com.capstone.traffic.ui.route.route.dataClass

data class SubwayData(
    val subwayStation : String,
    var startSubway : Boolean = false,
    var centerSubway : Boolean = false,
    var endSubway : Boolean = false,
    var rStartSubway : Boolean = false,
    var rCenterSubway : Boolean = false,
    var rEndSubway : Boolean = false
)
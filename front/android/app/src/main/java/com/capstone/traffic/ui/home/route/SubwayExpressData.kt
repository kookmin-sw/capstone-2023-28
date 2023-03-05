package com.capstone.traffic.ui.home.route

data class SubwayExpressData(
    val subwayStation : String,
    var eStartSubway : Boolean = false,
    var eCenterSubway : Boolean = false,
    var eEndSubway : Boolean = false,
    var eRStartSubway : Boolean = false,
    var eRCenterSubway : Boolean = false,
    var eREndSubway : Boolean = false,
    var startSubway : Boolean = false,
    var centerSubway : Boolean = false,
    var endSubway : Boolean = false,
    var rStartSubway : Boolean = false,
    var rCenterSubway : Boolean = false,
    var rEndSubway : Boolean = false
)
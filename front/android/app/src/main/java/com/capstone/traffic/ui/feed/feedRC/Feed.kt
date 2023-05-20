package com.capstone.traffic.ui.feed.feedRC

import com.capstone.traffic.model.network.sql.dataclass.getfeed.Images

data class Feed(
    val name: String,
    val time: String,
    val content : String,
    val image : List<Images>?
)

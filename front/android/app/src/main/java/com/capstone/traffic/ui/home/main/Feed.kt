package com.capstone.traffic.ui.home.main

import android.media.Image

data class Feed(
    val feedContent: String,
    val feedProfileImage: Image?,
    val feedNickname: String,
    val feedHashTag: List<String>,
    val feedTime: String,
)
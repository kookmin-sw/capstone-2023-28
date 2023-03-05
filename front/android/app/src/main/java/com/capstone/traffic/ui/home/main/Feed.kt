package com.capstone.traffic.ui.home.main

import android.media.Image

data class Feed(
    val feedContent: String,
    val feedProfileImageUrl: String?,
    val feedNickname: String,
    val feedHashTag: List<String>,
    val feedTime: String,
)
package com.capstone.traffic.ui.home.route.line
import android.content.Context
import android.util.AttributeSet

import android.view.LayoutInflater

import android.widget.LinearLayout
import com.capstone.traffic.R

class SubwayText : LinearLayout {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_subway_line_name, this, true)
    }
}
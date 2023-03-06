package com.capstone.traffic.global

import android.app.Application
import com.capstone.traffic.util.PreferenceUtil

class MyApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
        var status = false
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}
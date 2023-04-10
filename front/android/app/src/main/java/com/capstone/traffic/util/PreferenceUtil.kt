package com.capstone.traffic.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)

    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }

    fun setBoolean(key: String, boolean: Boolean) {
        prefs.edit().putBoolean(key, boolean).apply()
    }

    fun getToken() : String? {
        return prefs.getString("token", null)
    }

    fun setToken(token : String){
        prefs.edit().putString("token", token).apply()
    }
}

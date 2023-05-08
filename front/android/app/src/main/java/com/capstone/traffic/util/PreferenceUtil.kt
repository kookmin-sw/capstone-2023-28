package com.capstone.traffic.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory

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

    fun getEmail() : String? {
        return prefs.getString("email", null)
    }

    fun setEmail(email : String){
        prefs.edit().putString("email", email).apply()
    }

    fun setUserProfile(profile : String?){
        prefs.edit().putString("profile", profile).apply()
    }

    fun getUserProfile() : Bitmap? {
        return prefs.getString("profile", null)?.stringToBitmap()
    }

    private fun String.stringToBitmap() : Bitmap? {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}

package com.capstone.traffic.global

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth {
    companion object {
        private val TAG = Auth::class.java.simpleName
        private lateinit var auth: FirebaseAuth
        fun getAuth() : FirebaseAuth {
            auth = Firebase.auth
            return auth
        }
    }
}

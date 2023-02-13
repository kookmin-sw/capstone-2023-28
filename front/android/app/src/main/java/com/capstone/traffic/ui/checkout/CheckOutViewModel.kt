package com.capstone.traffic.ui.checkout

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.traffic.global.Auth

class CheckOutViewModel : ViewModel(){
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    private val _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean> get() = _status
    init {
        _status.value = false
    }
    fun createAccount()
    {
        if(email.get() != null && password.get() != null){
            Auth.getAuth().createUserWithEmailAndPassword(email.get()!!,password.get()!!).addOnCompleteListener {
                    task ->
                run {
                    if (task.isSuccessful) {
                        Log.d("login", "Success")
                        _status.value = true
                    } else {
                        Log.d("login", "fail")
                    }
                }
            }
        }
    }
}
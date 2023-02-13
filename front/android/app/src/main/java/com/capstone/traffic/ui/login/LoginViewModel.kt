package com.capstone.traffic.ui.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.traffic.global.Auth
import com.capstone.traffic.global.MyApplication
class LoginViewModel : ViewModel(){
    val email = ObservableField<String>()
    val password = ObservableField<String>()

    private val _loginStatus = MutableLiveData<Boolean>()

    val loginStatus : LiveData<Boolean> get() = _loginStatus

    init {
        _loginStatus.value = MyApplication.prefs.getBoolean("status")
    }

    fun signIn()
    {
        if(email.get() != null && password.get() != null){
            Auth.getAuth().signInWithEmailAndPassword(email.get()!!, password.get()!!)?.addOnCompleteListener {
                task ->
                run {
                    if(task.isSuccessful) {
                        _loginStatus.value = true
                        MyApplication.prefs.setBoolean("status",true)
                    }
                }
            }
        }
    }
}
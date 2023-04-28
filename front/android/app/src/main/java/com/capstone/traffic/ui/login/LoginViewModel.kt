package com.capstone.traffic.ui.login

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sql.AuthClient
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.login.LoginResFail
import com.capstone.traffic.model.network.sql.dataclass.login.LoginResSuc
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader

class LoginViewModel(application: Application) : AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
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
            login()
        }
    }

    private fun login(){
        val retrofit = AuthClient.getInstance()
        val loginService = retrofit.create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val param  = RequestBody.create(mediaType,"{\"password\":\"${password.get()}\",\"user_email\":\"${email.get()}\"}")
        loginService.getLogin(param = param)
            .enqueue(object : Callback<LoginResSuc> {
                override fun onResponse(call: Call<LoginResSuc>, response: Response<LoginResSuc>) {
                    if (response.isSuccessful){
                        MyApplication.prefs.setToken(response.body()!!.res.access)
                        MyApplication.prefs.setBoolean("status",true)
                        MyApplication.prefs.setEmail(email.get().toString())
                        _loginStatus.value = true
                        Toast.makeText(context, "로그인 완료", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val reader = BufferedReader(response.errorBody()?.source()!!.inputStream().reader())
                        val content = StringBuilder()
                        reader.use { readerBuffer ->
                            var line = readerBuffer.readLine()
                            while (line != null) {
                                content.append(line)
                                line = readerBuffer.readLine()
                            }
                        }
                        val a = Gson().fromJson(content.toString(), LoginResFail::class.java)
                        if(response.errorBody() != null) Toast.makeText(context, "로그인 실패 : ${a?.res?.errorName}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<LoginResSuc>, t: Throwable) {
                }
            })
    }
}
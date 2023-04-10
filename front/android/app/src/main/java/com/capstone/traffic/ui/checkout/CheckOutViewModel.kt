package com.capstone.traffic.ui.checkout

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.sign.SignResSuc
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader

class CheckOutViewModel(application : Application) : AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val nickname = ObservableField<String>()

    private val _status = MutableLiveData<Boolean>()

    val status : LiveData<Boolean> get() = _status
    init {
        _status.value = false
    }
    fun createAccount()
    {
        if(email.get() != null && password.get() != null && nickname.get() != null){
            getSignUp()
        }
    }

    private fun getSignUp(){
        val retrofit = Client.getInstance()
        val signUpService = retrofit.create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val param  = RequestBody.create(mediaType,"{\"password\":\"${password.get()}\",\"user_email\":\"${email.get()}\",\"user_nickname\":\"${nickname.get()}\"}")
        signUpService.getSign(param = param)
            .enqueue(object : Callback<SignResSuc> {
                override fun onResponse(call: Call<SignResSuc>, response: Response<SignResSuc>) {
                    if (response.isSuccessful){
                        Toast.makeText(context, "회원가입 완료", Toast.LENGTH_SHORT).show()
                        _status.value = true
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
                        val a = Gson().fromJson(content.toString(), SignResSuc::class.java)
                        if(response.errorBody() != null) Toast.makeText(context, "회원가입 실패 : ${a.res.errorName}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<SignResSuc>, t: Throwable) {
                }
            })
    }
}
package com.capstone.traffic.ui.my

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sql.AuthClient
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MyViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _nickname = MutableLiveData<String>()
    private val _post =  MutableLiveData<Int>()
    private val _follower =  MutableLiveData<Int>()
    private val _following =  MutableLiveData<Int>()
    private val _userDefini = MutableLiveData<String>()
    private val _profile = MutableLiveData<String>()

    val profile : LiveData<String> = _profile
    val userDefini : LiveData<String> = _userDefini
    val nickname : LiveData<String> = _nickname
    val post : LiveData<Int> = _post
    val follower : LiveData<Int> = _follower
    val following : LiveData<Int> = _following

    init {
        getUserData()
        getUserAPI()
    }

    private fun getUserData()
    {
        // 임시로 데이터 설정
        _post.value = 2
        _follower.value = 53
        _following.value = 65
    }

    fun getUserAPI(){
        val retrofit = AuthClient.getInstance()
        val infoService = retrofit.create(Service::class.java)
        infoService.getInfo(userEmail = MyApplication.prefs.getEmail().toString(), userNickname = null, pageNum = null).enqueue(object : Callback<InfoRecSuc>{
            override fun onResponse(call: Call<InfoRecSuc>, response: Response<InfoRecSuc>) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null) {
                        _nickname.value = data.res[0].userNickName
                        _userDefini.value = data.res[0].userDefinition
                        _profile.value = data.res[0].user_profile_image ?: ""
                    }
                }
            }
            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {
                print(1)
            }
        })
    }
}
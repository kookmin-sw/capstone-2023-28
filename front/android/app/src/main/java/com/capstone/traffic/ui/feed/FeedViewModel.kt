package com.capstone.traffic.ui.feed

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide.get
import com.bumptech.glide.Glide.init
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.global.viewmodel.BaseViewModel
import com.capstone.traffic.model.network.news.Client
import com.capstone.traffic.model.network.sql.AuthClient
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedViewModel() : BaseViewModel(){

    private val _itemSelectedNum = MutableLiveData<View>()
    val itemSelectedNum get() = _itemSelectedNum

    private val _profile = MutableLiveData<String>()
    val profile : LiveData<String> = _profile

    companion object {
        const val EVENT_START_FILTER_APPLY = 22212
        const val EVENT_START_FILTER_SELECT = 22213
    }

    init {
        getUserAPI()
    }
    fun getUserAPI(){
        val retrofit = Client.getInstance()
        val infoService = retrofit.create(Service::class.java)
        infoService.getInfo(userEmail = MyApplication.prefs.getEmail().toString(), userNickname = null, pageNum = null).enqueue(object : Callback<InfoRecSuc>{
            override fun onResponse(call: Call<InfoRecSuc>, response: Response<InfoRecSuc>) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null) {
                        _profile.value = data.res[0].user_profile_image ?: ""
                    }
                }
            }
            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {
                print(1)
            }
        })
    }

    fun filterApply() = viewEvent(EVENT_START_FILTER_APPLY)
    fun filterSelect(view : View) {
        itemSelectedNum.value = view
        viewEvent(EVENT_START_FILTER_SELECT)
    }
}
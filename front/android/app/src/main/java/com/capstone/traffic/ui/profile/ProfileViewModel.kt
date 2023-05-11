package com.capstone.traffic.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.getfeed.FeedResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Res
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


class ProfileViewModel : ViewModel() {

    private val _userNickName = MutableLiveData<String>()
    private val _userProfile = MutableLiveData<String>()
    private val _userDefinition = MutableLiveData<String>()
    private val _feedData = MutableLiveData<List<Res>>()

    val userNickname : LiveData<String> = _userNickName
    val userProfile : LiveData<String> = _userProfile
    val userDefinition : LiveData<String> = _userDefinition
    val feedData : LiveData<List<Res>> = _feedData

    init {

    }

    fun getFeedData(userName : String) {
        val feedService = Client.getInstance().create(Service::class.java)
        feedService.getFeed(null,null,userName).enqueue(object : Callback<FeedResSuc>{
            override fun onResponse(call: Call<FeedResSuc>, response: Response<FeedResSuc>) {
                if(response.isSuccessful){
                    _feedData.value = response.body()?.res
                }
            }

            override fun onFailure(call: Call<FeedResSuc>, t: Throwable) {

            }
        })

    }
    fun getUserInfo(userName : String) {

        _userNickName.value = userName

        val infoService = Client.getInstance().create(Service::class.java)
        infoService.getInfo(null, userNickname = userName, "1").enqueue(object :Callback<InfoRecSuc>{
            override fun onResponse(call: Call<InfoRecSuc>, response: Response<InfoRecSuc>) {
                if(response.isSuccessful){
                    if(response.body()?.res?.size != 0){
                        val userData = response.body()?.res!![0]
                        _userDefinition.value = userData.userDefinition
                        _userProfile.value = userData.user_profile_image
                        getFeedData(userName = userData.userNickName)
                    }
                }
            }

            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {

            }
        })
    }


}
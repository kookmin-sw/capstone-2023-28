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
    private val _userProfile = MutableLiveData<String?>()
    private val _userDefinition = MutableLiveData<String>()
    private val _feedData = MutableLiveData<MutableList<Res>>()
    private val _follower =  MutableLiveData<String>()
    private val _following =  MutableLiveData<String>()
    private val _isFollowing = MutableLiveData<Boolean>()
    private val _id = MutableLiveData<String>()
    private val _feedNum = MutableLiveData<String>()

    val userNickname : LiveData<String> = _userNickName
    val userProfile : LiveData<String?> = _userProfile
    val userDefinition : LiveData<String> = _userDefinition
    val feedData : LiveData<MutableList<Res>> = _feedData
    val follower : LiveData<String> = _follower
    val following : LiveData<String> = _following
    val isFollowing : LiveData<Boolean> = _isFollowing
    val id : LiveData<String> = _id
    val feedNum : LiveData<String> = _feedNum


    init {
        _feedData.value = mutableListOf()
    }

    fun getFeedData(userName : String, pageIndex: String) {
        val feedService = Client.getInstance().create(Service::class.java)
        feedService.getFeed(null,null,userName,"5",pageIndex).enqueue(object : Callback<FeedResSuc>{
            override fun onResponse(call: Call<FeedResSuc>, response: Response<FeedResSuc>) {
                if(response.isSuccessful){
                    if(response.body()?.res != null){
                        _feedData.value = response.body()?.res!! as MutableList<Res>
                    }
                }
            }

            override fun onFailure(call: Call<FeedResSuc>, t: Throwable) {

            }
        })

    }
    fun getUserInfo(userName : String?, pageIndex : String, updateFeed: Boolean = true) {

        if(userName != null) _userNickName.value = userName!!

        val infoService = Client.getInstance().create(Service::class.java)
        infoService.getInfo(null, userNickname = _userNickName.value, "1").enqueue(object :Callback<InfoRecSuc>{
            override fun onResponse(call: Call<InfoRecSuc>, response: Response<InfoRecSuc>) {
                if(response.isSuccessful){
                    if(response.body()?.res?.size != 0){
                        val userData = response.body()?.res!![0]
                        _userDefinition.value = userData.userDefinition
                        if(userData.user_profile_image != null) _userProfile.value = userData.user_profile_image
                        _follower.value = userData.followerNum
                        _following.value = userData.followingNum
                        _isFollowing.value = userData.isFollower.toBoolean()
                        _id.value = userData.id
                        _feedNum.value = userData.feedNum
                        if(updateFeed) getFeedData(userName = userData.userNickName, pageIndex)
                    }
                }
            }

            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {

            }
        })
    }


}
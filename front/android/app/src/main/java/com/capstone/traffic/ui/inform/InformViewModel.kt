package com.capstone.traffic.ui.inform

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.capstone.traffic.model.network.demon.Data
import com.capstone.traffic.model.network.demon.DemonClient
import com.capstone.traffic.model.network.demon.DemonService
import com.capstone.traffic.model.network.demon.Response
import com.capstone.traffic.model.network.news.News
import com.capstone.traffic.model.network.seoulMetro.Res
import com.capstone.traffic.model.network.seoulMetro.Service
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.twitter.Client
import com.capstone.traffic.model.network.twitter.RankService
import com.capstone.traffic.model.network.twitter.ResponseData
import retrofit2.Call
import retrofit2.Callback

class InformViewModel : ViewModel() {
    private val _demonData = MutableLiveData<MutableList<Data>>()
    private val _newsData = MutableLiveData<MutableList<News>>()
    private val _twitData = MutableLiveData<MutableList<com.capstone.traffic.model.network.twitter.Data>>()
    private val _rankData = MutableLiveData<MutableList<com.capstone.traffic.model.network.seoulMetro.Data>>()
    val demonData : LiveData<MutableList<Data>> = _demonData
    val newsData : LiveData<MutableList<News>> = _newsData
    val twitData : LiveData<MutableList<com.capstone.traffic.model.network.twitter.Data>> = _twitData
    val rankData : LiveData<MutableList<com.capstone.traffic.model.network.seoulMetro.Data>> = _rankData

    init {
        getNews()
        getRankApi()
        getDemonApi()
        getTwitApi()
    }
    private fun getNews()
    {
        val newsService = com.capstone.traffic.model.network.news.Client.getInstance().create(com.capstone.traffic.model.network.news.Service::class.java)
        newsService.getNews().enqueue( object : Callback<com.capstone.traffic.model.network.news.Res>{
            override fun onResponse(
                call: Call<com.capstone.traffic.model.network.news.Res>,
                response: retrofit2.Response<com.capstone.traffic.model.network.news.Res>
            ) {
                if(response.isSuccessful)
                {
                    val newData = response.body()?.data
                    _newsData.value = newData as MutableList<News>
                }
            }

            override fun onFailure(
                call: Call<com.capstone.traffic.model.network.news.Res>,
                t: Throwable
            ) {

            }
        }
        )
    }
    private fun getRankApi() {
        val retrofit = Client.getInstance()
        val service = retrofit.create(RankService::class.java)
        service.getResponse()
            .enqueue(object : Callback<ResponseData> {
                override fun onResponse(
                    call: Call<ResponseData>,
                    response: retrofit2.Response<ResponseData>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!.data
                        _twitData.value = data as MutableList<com.capstone.traffic.model.network.twitter.Data>
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                }
            })
    }
    private fun getTwitApi()
    {
        val retrofit = com.capstone.traffic.model.network.seoulMetro.Client.getInstance()
        val service = retrofit.create(Service::class.java)

        service.getTwit().enqueue(object : Callback<Res>{
            override fun onResponse(call: Call<Res>, response: retrofit2.Response<Res>) {
                if(response.isSuccessful){
                    if(response.body() != null) _rankData.value = response.body()!!.data as MutableList<com.capstone.traffic.model.network.seoulMetro.Data>
                }
            }

            override fun onFailure(call: Call<Res>, t: Throwable) {
            }
        })
    }
    private fun getDemonApi() {
        val retrofit = DemonClient.getInstance()
        val service = retrofit.create(DemonService::class.java)
        service.getResponse()
            .enqueue(object : Callback<Response> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.Data
                        _demonData.value =  data as MutableList<com.capstone.traffic.model.network.demon.Data>
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                }
            })
    }


}
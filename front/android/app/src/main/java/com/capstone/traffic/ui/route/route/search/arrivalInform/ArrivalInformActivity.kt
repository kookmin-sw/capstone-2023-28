package com.capstone.traffic.ui.route.route.search.arrivalInform

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityArrivalInformBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.model.network.seoul.SeoulClient
import com.capstone.traffic.model.network.seoul.arrive.RealtimeArrivalList
import com.capstone.traffic.model.network.seoul.arrive.Seoul
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArrivalInformActivity : BaseActivity<ActivityArrivalInformBinding>() {
    override var layoutResourceId: Int = R.layout.activity_arrival_inform
    private lateinit var arrivalInformViewModel : ArrivalInformViewModel
    private lateinit var adapter: ArrivalInformAdapter
    override fun initBinding() {
        arrivalInformViewModel = ViewModelProvider(this)[ArrivalInformViewModel::class.java]
        binding.arrivalInform = arrivalInformViewModel

        val stationName = intent.getStringExtra("station")
        binding.stationNameTv.text = stationName

        binding.arrivalRv.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL,false)
        adapter = ArrivalInformAdapter(this)
        binding.arrivalRv.adapter = adapter

        getSeoulApi(stationName.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecyclerView(datas : List<RealtimeArrivalList>)
    {
        adapter.datas = datas.sortedBy { it.subwayId }
        adapter.notifyDataSetChanged()
    }

    fun getSeoulApi(station : String) {
        val retrofit = SeoulClient.getInstance()
        val seoulService = retrofit.create(com.capstone.traffic.model.network.seoul.arrive.SeoulService::class.java)
        seoulService.getInfo("http://swopenapi.seoul.go.kr/api/subway/${APIKEY.SEOUL_APIKEY}/json/realtimeStationArrival/0/1000/${station}")
            .enqueue(object : Callback<Seoul> {
                override fun onResponse(call: Call<Seoul>, response: Response<Seoul>) {
                    if (response.isSuccessful) {
                        val datas = response.body()?.realtimeArrivalList
                        if (datas != null) setRecyclerView(datas)
                    }
                }

                override fun onFailure(call: Call<Seoul>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }
}
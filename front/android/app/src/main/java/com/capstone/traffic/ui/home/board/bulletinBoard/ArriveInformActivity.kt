package com.capstone.traffic.ui.home.board.bulletinBoard

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityBulletinBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.global.stationInfo.station
import com.capstone.traffic.model.network.seoul.SeoulClient
import com.capstone.traffic.model.network.seoul.arrive.RealtimeArrivalList
import com.capstone.traffic.model.network.seoul.arrive.Seoul
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.Files.find

class ArriveInformActivity : BaseActivity<ActivityBulletinBinding>(){
    override var layoutResourceId: Int = R.layout.activity_bulletin
    private lateinit var bulletinViewModel : BulletinViewModel
    private lateinit var beforeStation : TextView
    private lateinit var nextStation : TextView
    private lateinit var direct : Direct

    val direct1Data = mutableListOf<ArrivalInform>()
    val direct2Data = mutableListOf<ArrivalInform>()

    override fun initBinding() {
        bulletinViewModel = ViewModelProvider(this)[BulletinViewModel::class.java]
        binding.inform = bulletinViewModel

        val name = intent.getStringExtra("name")!!
        val line = intent.getStringExtra("line")!!

        setBoard(name, line)

        binding.ctTV.text = name
        beforeStation = binding.bfTV
        nextStation = binding.edTV

        // 상단바 벡버튼 기능
        binding.backBTN.setOnClickListener {
            finish()
        }
        getSeoulApi(name)
    }

    // 게시판 이름 설정
    @SuppressLint("SetTextI18n")
    fun setBoard(name : String, line : String) {
        binding.boardTV.text = "${line}호선 ${name}역 게시판"
    }

    // 도착 정보 리사이클러 뷰 설정
    @SuppressLint("NotifyDataSetChanged")
    fun setInformRecyclerView(view : RecyclerView, data : MutableList<ArrivalInform>){
        val adapter = informAdapter(applicationContext, R.layout.inform_item)
        adapter.datas = data
        view.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL,false)
        view.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun informDataParse(data : List<RealtimeArrivalList>) {
        val direct1Data = mutableListOf<ArrivalInform>()
        val direct2Data = mutableListOf<ArrivalInform>()
        data.forEach { it ->
            val trainLineNm = it.trainLineNm.split(" - ")
            when(trainLineNm[0].dropLast(1)){
                direct.first ->{
                    direct1Data.add(ArrivalInform(trainLineNm[1], it.arvlMsg2))
                    setInformRecyclerView(binding.dwRC, direct1Data)
                }
                direct.last ->{
                    direct2Data.add(ArrivalInform(trainLineNm[1], it.arvlMsg2))
                    setInformRecyclerView(binding.upRC, direct2Data)
                }
            }
        }


    }

    // API
    fun getSeoulApi(station : String) {
        val retrofit = SeoulClient.getInstance()
        val seoulService = retrofit.create(com.capstone.traffic.model.network.seoul.arrive.SeoulService::class.java)
        seoulService.getInfo("http://swopenapi.seoul.go.kr/api/subway/${APIKEY.SEOUL_APIKEY}/json/realtimeStationArrival/0/1000/${station}")
            .enqueue(object : Callback<Seoul> {
                override fun onResponse(call: Call<Seoul>, response: Response<Seoul>) {

                    if (response.isSuccessful) {
                        val data = response.body()?.realtimeArrivalList
                        if (data != null) informDataParse(data)
                        }
                        else {
                            setInformRecyclerView(binding.dwRC, mutableListOf())
                            setInformRecyclerView(binding.upRC, mutableListOf())
                        }
                    }

                override fun onFailure(call: Call<Seoul>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    data class Direct(val first : String, val last : String)
}
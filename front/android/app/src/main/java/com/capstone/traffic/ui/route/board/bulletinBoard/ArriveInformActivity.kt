package com.capstone.traffic.ui.route.board.bulletinBoard

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityBulletinBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.model.network.seoul.SeoulClient
import com.capstone.traffic.model.network.seoul.arrive.RealtimeArrivalList
import com.capstone.traffic.model.network.seoul.arrive.Seoul
import com.capstone.traffic.ui.route.route.dataClass.NeighborLineData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArriveInformActivity : BaseActivity<ActivityBulletinBinding>(){
    override var layoutResourceId: Int = R.layout.activity_bulletin
    private lateinit var bulletinViewModel : BulletinViewModel

    lateinit var nlData : NeighborLineData

    override fun initBinding() {
        bulletinViewModel = ViewModelProvider(this)[BulletinViewModel::class.java]
        binding.inform = bulletinViewModel

        nlData = intent.getParcelableExtra("neighbor")!!

        setBoard(nlData.center, nlData.line)
        getSeoulApi(nlData.center)
    }

    // 게시판 이름 설정
    private fun setBoard(name : String, line : String) {
        val drawable = binding.centerStationImageView.background as GradientDrawable
        drawable.setColor(getStationColor(line))
        binding.centerStationTextView.text = "${name}역"
        binding.startStationTextView.text = if(nlData.left != null) "${nlData.left}역" else ""
        binding.endStationTextView.text = if(nlData.right != null) "${nlData.right}역" else ""
        binding.direct1.text = if(nlData.left != null) "${nlData.left}방면" else ""
        binding.direct2.text = if(nlData.right != null) "${nlData.right}방면" else ""
        binding.centerStationImageView.setImageDrawable(drawable)
        binding.startStationImageView.setBackgroundColor(getStationColor(line))
        binding.endStationImageView.setBackgroundColor(getStationColor(line))
    }

    private fun getStationColor(line : String) : Int {
        val color = mapOf("1" to R.color.hs1,
            "2" to R.color.hs2,
            "3" to R.color.hs3,
            "4" to R.color.hs4,
            "5" to R.color.hs5,
            "6" to R.color.hs6,
            "7" to R.color.hs7,
            "8" to R.color.hs8,
            "9" to R.color.hs9)
        return if(color[line] != null) resources.getColor(color[line]!!) else resources.getColor(R.color.black)
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
        // 2호선만 외선 내선이기 때문에 다르게 처리해야함.
        if (nlData.line == "2"){
            data.forEach { it ->
                val trainLineNm = it.trainLineNm.split(" - ")
                val arrivalInform = ArrivalInform(arrow = it.updnLine,null,null)
                if("100${nlData.line}" == it.subwayId){
                    val time = it.arvlMsg2.split(" ")
                    when(time[1]){
                        "진입" -> {
                            arrivalInform.priority = if (time[0] == "전역") -1 else -4
                            arrivalInform.time = it.arvlMsg2
                        }
                        "도착" -> {
                            arrivalInform.priority = if (time[0] == "전역") -2 else -5
                            arrivalInform.time = it.arvlMsg2
                        }
                        "출발" -> {
                            arrivalInform.priority = if (time[0] == "전역") -3 else -6
                            arrivalInform.time = it.arvlMsg2
                        }
                        else -> {
                            arrivalInform.priority = time[0].dropLast(1).toInt()
                            arrivalInform.time = time[0]
                        }
                    }
                    when(trainLineNm[1].dropLast(2)){
                        nlData.right ->{direct1Data.add(arrivalInform) }
                        nlData.left ->{direct2Data.add(arrivalInform) }
                    }
                }
            }
            direct1Data.sortBy(ArrivalInform::priority)
            direct2Data.sortBy(ArrivalInform::priority)
            setInformRecyclerView(binding.dwRC, direct1Data)
            setInformRecyclerView(binding.upRC, direct2Data)
        }
        else {
            data.forEach { it ->
                val trainLineNm = it.trainLineNm.split(" - ")
                val arrivalInform = ArrivalInform(arrow = trainLineNm[0],null,null)
                if("100${nlData.line}" == it.subwayId){
                    val time = it.arvlMsg2.split(" ")
                    if(time.size == 1){
                        arrivalInform.priority = -7
                        arrivalInform.time = it.arvlMsg2
                    }
                    else
                    {
                        when(time[1]){
                            "진입" -> {
                                arrivalInform.priority = if (time[0] == "전역") -1 else -4
                                arrivalInform.time = it.arvlMsg2
                            }
                            "도착" -> {
                                arrivalInform.priority = if (time[0] == "전역") -2 else -5
                                arrivalInform.time = it.arvlMsg2
                            }
                            "전역" -> {
                                val remain =
                                    if (time[0].length == 5) time[0].slice((1..1)) else time[0].slice((1..2))
                                arrivalInform.time = "${remain} 전역"
                                arrivalInform.priority = remain.toInt()
                            }
                            "출발" -> {
                                arrivalInform.priority = if (time[0] == "전역") -3 else -6
                                arrivalInform.time = it.arvlMsg2
                            }
                            else -> {
                                arrivalInform.priority = time[0].dropLast(1).toInt()
                                arrivalInform.time = time[0]
                            }
                        }
                    }
                    when(trainLineNm[1].dropLast(2)){
                        nlData.right ->{direct1Data.add(arrivalInform) }
                        nlData.left ->{direct2Data.add(arrivalInform) }
                    }
                }
            }
            direct1Data.sortBy(ArrivalInform::priority)
            direct2Data.sortBy(ArrivalInform::priority)
            setInformRecyclerView(binding.dwRC, direct1Data)
            setInformRecyclerView(binding.upRC, direct2Data)
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
}
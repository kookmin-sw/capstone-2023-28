package com.capstone.traffic.ui.home.route.line

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentLine3Binding
import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.global.stationInfo.station
import com.capstone.traffic.model.network.seoul.locate.RealtimePositionList
import com.capstone.traffic.model.network.seoul.locate.Seoul
import com.capstone.traffic.model.network.seoul.SeoulClient
import com.capstone.traffic.model.network.seoul.locate.SeoulService
import com.capstone.traffic.ui.home.route.SubwayAdapter
import com.capstone.traffic.ui.home.route.SubwayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Line3Fragment : Fragment() {

    private val binding by lazy { FragmentLine3Binding.inflate(layoutInflater) }

    lateinit var subwayData: LinkedHashMap<String, SubwayData>
    lateinit var subwayAdapter: SubwayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // 리사이클러뷰 설정
        initRecyclerView()
        setRecyclerView()

        getSeoulApi()
        return binding.root
    }


    private fun initRecyclerView() {
        subwayData = LinkedHashMap()
        station.getLine3All().forEach { subwayData.put(it, SubwayData(it)) }
        subwayAdapter = SubwayAdapter(requireContext(), R.layout.subway_line_three_item,"3")

        binding.RVSubwayMain.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.RVSubwayMain.adapter = subwayAdapter
    }

    private fun dataToList(data: LinkedHashMap<String, SubwayData>): MutableList<SubwayData> {
        val tmp = mutableListOf<SubwayData>()
        tmp.addAll(data.values)
        return tmp
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRecyclerView() {
        subwayAdapter.datas = dataToList(subwayData)
        subwayAdapter.notifyDataSetChanged()
    }

    fun getSeoulApi() {
        val retrofit = SeoulClient.getInstance()
        val seoulService = retrofit.create(SeoulService::class.java)
        seoulService.getInfo("http://swopenapi.seoul.go.kr/api/subway/${APIKEY.SEOUL_APIKEY}/json/realtimePosition/0/1000/3호선")
            .enqueue(object : Callback<Seoul> {
                override fun onResponse(call: Call<Seoul>, response: Response<Seoul>) {

                    if (response.isSuccessful) {
                        val data = response.body()?.realtimePositionList
                        if (data != null) dataMatching(data)
                        else setRecyclerView()
                    }
                }

                override fun onFailure(call: Call<Seoul>, t: Throwable) {
                }
            })
    }
    fun dataMatching(data : List<RealtimePositionList>)
    {
        data.forEachIndexed { index, value ->
            val name = value.statnNm
            val tmp = subwayData[name]
            if(tmp != null){
                when(value.updnLine)
                {
                    "0" -> {
                        when(value.trainSttus)
                        {
                            "0" -> {
                                tmp.rEndSubway = true
                                subwayData.replace(name,tmp)
                            }
                            "1" -> {
                                tmp.rCenterSubway = true
                                subwayData.replace(name,tmp)
                            }
                            else -> {
                                tmp.rStartSubway = true
                                subwayData.replace(name,tmp)
                            }
                        }
                    }
                    "1" -> {
                        when(value.trainSttus)
                        {
                            "0" -> {
                                tmp.startSubway = true
                                subwayData.replace(name,tmp)
                            }
                            "1" -> {
                                tmp.centerSubway = true
                                subwayData.replace(name,tmp)
                            }
                            else -> {
                                tmp.endSubway = true
                                subwayData.replace(name,tmp)
                            }
                        }
                    }
                }
            }
        }
        subwayAdapter.notifyDataSetChanged()
    }
}


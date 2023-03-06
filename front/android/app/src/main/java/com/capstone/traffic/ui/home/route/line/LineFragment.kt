package com.capstone.traffic.ui.home.route.line

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentLineBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.subway.Client
import com.capstone.traffic.model.network.subway.ResponseList
import com.capstone.traffic.model.network.subway.SubwayService
import com.capstone.traffic.ui.home.route.RouteFragment
import com.capstone.traffic.ui.home.route.SubwayAdapter
import com.capstone.traffic.ui.home.route.dataClass.SubwayData
import com.capstone.traffic.ui.home.route.SubwayExpressAdapter
import com.capstone.traffic.ui.home.route.dataClass.SubwayExpressData
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LineFragment(var searchLine : String) : Fragment() {
    private val binding by lazy { FragmentLineBinding.inflate(layoutInflater) }
    private var job : Job? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.lottieLoading.playAnimation()
        binding.lottieLoading.loop(true)
        getApi()
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun startLoading(rl: List<ResponseList>) {
        job = GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                initSubwayLineView(rl)
            }
            endLoading()
        }
    }
    private fun endLoading() {
        //
        binding.lottieLoading.visibility = View.INVISIBLE
        binding.lottieLoading.loop(false)
        job?.cancel()
        MyApplication.status = false
    }

    // 리사이클러뷰 생성
    private fun addExPressRecyclerView(sd: List<SubwayExpressData>) : View
    {
        val recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.isNestedScrollingEnabled = false
        val subwayAdapter = SubwayExpressAdapter(requireContext(), R.layout.express_subway_item,searchLine)
        subwayAdapter.datas = sd
        recyclerView.adapter = subwayAdapter
        return recyclerView

    }

    private fun addRecyclerView(sd : List<SubwayData>) : View
    {
        val recyclerView = RecyclerView(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.isNestedScrollingEnabled = false
        val subwayAdapter = SubwayAdapter(requireContext(), R.layout.express_not_subway_item, searchLine)
        subwayAdapter.datas = sd
        recyclerView.adapter = subwayAdapter
        return recyclerView
    }

    // 텍스트 뷰 동적 생성
    private fun addTextView(name : String) : View
    {
        val textView = TextView(requireContext())
        textView.text = name
        textView.background =  ContextCompat.getDrawable(requireContext(), R.drawable.background_subway_text)
        textView.textSize = 12f
        textView.gravity = Gravity.CENTER or Gravity.START
        textView.typeface = android.graphics.Typeface.DEFAULT_BOLD
        textView.setTextColor(resources.getColor(R.color.black))
        textView.setPadding(15,0,0,0)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDp(20f).toInt()).apply {setMargins(15,15,15,15)}
        textView.layoutParams = lp
        return textView
    }

    // dp -> px
    private fun getDp(value : Float) : Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    }

    private fun initSubwayLineView(data : List<ResponseList>) {
        data.forEach {rl ->
            binding.parent.addView(addTextView(rl.line))
            if(rl.express == "1"){
                val subData = ArrayList<SubwayExpressData>()
                rl.value.forEach { vl ->
                    val stList = vl.status
                    val sed = SubwayExpressData(subwayStation = vl.name)
                    when(stList[1]){
                        0 -> {}
                        1 -> sed.startSubway = true
                        2 -> sed.centerSubway = true
                        else -> sed.endSubway = true
                    }
                    when(stList[0]){
                        0 -> {}
                        2 -> sed.rCenterSubway = true
                        1 -> sed.rEndSubway = true
                        else -> sed.rStartSubway = true
                    }
                    when(stList[3]){
                        0 -> {}
                        1 -> sed.eStartSubway = true
                        2 -> sed.eCenterSubway = true
                        else -> sed.eEndSubway = true
                    }
                    when(stList[2]){
                        0 -> {}
                        2 -> sed.eRCenterSubway = true
                        1 -> sed.eREndSubway = true
                        else -> sed.eRStartSubway = true
                    }
                    subData.add(sed)
                }
                binding.parent.addView(addExPressRecyclerView(subData))
            }
            else if(rl.express == "0"){
                val subData = ArrayList<SubwayData>()
                rl.value.forEach { vl ->
                    val stList = vl.status
                    val sed = SubwayData(subwayStation = vl.name)
                    when(stList[1]){
                        0 -> {}
                        1 -> sed.startSubway = true
                        2 -> sed.centerSubway = true
                        else -> sed.endSubway = true
                    }
                    when(stList[0]){
                        0 -> {}
                        2 -> sed.rCenterSubway = true
                        1 -> sed.rEndSubway = true
                        else -> sed.rStartSubway = true
                    }
                    subData.add(sed)
                }
                binding.parent.addView(addRecyclerView(subData))
            }
        }
    }

    private fun getApi() {
        val retrofit = Client.getInstance()
        val service = retrofit.create(SubwayService::class.java)
        service.getResponse(searchLine)
            .enqueue(object : Callback<com.capstone.traffic.model.network.subway.Response> {
                override fun onResponse(call: Call<com.capstone.traffic.model.network.subway.Response>, response: Response<com.capstone.traffic.model.network.subway.Response>) {
                    if(response.isSuccessful) startLoading(response.body()!!.response)
                }

                override fun onFailure(call: Call<com.capstone.traffic.model.network.subway.Response>, t: Throwable) {
                    println(call)
                }
            })
    }
}
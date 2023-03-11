package com.capstone.traffic.ui.home.route.line

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentLineBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.subway.Client
import com.capstone.traffic.model.network.subway.ResponseList
import com.capstone.traffic.model.network.subway.SubwayService
import com.capstone.traffic.ui.home.route.SubwayAdapter
import com.capstone.traffic.ui.home.route.SubwayExpressAdapter
import com.capstone.traffic.ui.home.route.dataClass.SubwayData
import com.capstone.traffic.ui.home.route.dataClass.SubwayExpressData
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LineFragment(var searchLine : String) : Fragment() {
    private val binding by lazy { FragmentLineBinding.inflate(layoutInflater) }
    private var job : Job? = null
    private var apiAns = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setLottie()
        getApi()
        return binding.root
    }
    private fun addLineView(name: String, express : Boolean, sd1 : List<SubwayExpressData>?, sd2 : List<SubwayData>?){
        val mainColor = getStationColor(searchLine)
        val subText = SubwayText(requireContext())
        val tv = subText.findViewById<TextView>(R.id.tv)
        tv.text = name

        val rv = subText.findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv.isNestedScrollingEnabled = false
        if(express){
            val subwayAdapter =  SubwayExpressAdapter(requireContext(), R.layout.express_subway_item, searchLine)
            subwayAdapter.datas = sd1!!
            rv.adapter = subwayAdapter
        }
        else{
            val subwayAdapter = SubwayAdapter(requireContext(),R.layout.express_not_subway_item, searchLine)
            subwayAdapter.datas = sd2!!
            rv.adapter = subwayAdapter
        }

        val cv = subText.findViewById<CardView>(R.id.cv)
        cv.backgroundTintList = ColorStateList.valueOf(mainColor);
        val iv = subText.findViewById<AppCompatImageView>(R.id.iv)
        cv.setOnClickListener {
            if(rv.visibility == View.GONE){
                iv.background = resources.getDrawable(R.drawable.icon_hide)
                rv.visibility = View.VISIBLE
            }
            else{
                iv.background = resources.getDrawable(R.drawable.icon_show)
                rv.visibility = View.GONE
            }
        }
        binding.parent.addView(subText)
    }
    private fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
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
        if(apiAns) {
            binding.lottieLoading.visibility = View.INVISIBLE
            binding.lottieLoading.loop(false)
            MyApplication.status = false
        }else{
            binding.lottieLoading.setAnimation(R.raw.icon_404_error)
            binding.lottieLoading.playAnimation()
            binding.lottieLoading.loop(true)
            MyApplication.status = false
        }
        job?.cancel()
    }


    private fun setLottie() {
        binding.lottieLoading.visibility = View.VISIBLE
        binding.lottieLoading.setAnimation(R.raw.lottie_loading)
        binding.lottieLoading.playAnimation()
        binding.lottieLoading.loop(true)
    }

    private fun getDp(value : Float) : Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
    }

    private fun initSubwayLineView(data : List<ResponseList>) {
        data.forEach {rl ->
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
                addLineView(rl.line, true, subData, null)
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
                addLineView(rl.line, false, null, subData)
            }
        }
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
        return if(color[line] != null) requireContext().resources.getColor(color[line]!!) else requireContext().resources.getColor(R.color.black)
    }
    private fun getApi() {
        val retrofit = Client.getInstance()
        val service = retrofit.create(SubwayService::class.java)
        service.getResponse(searchLine)
            .enqueue(object : Callback<com.capstone.traffic.model.network.subway.Response> {
                override fun onResponse(call: Call<com.capstone.traffic.model.network.subway.Response>, response: Response<com.capstone.traffic.model.network.subway.Response>) {
                    if(response.isSuccessful) {
                        apiAns = true
                        startLoading(response.body()!!.response)
                    }
                    else {
                        apiAns = false
                        endLoading()
                    }
                }
                override fun onFailure(call: Call<com.capstone.traffic.model.network.subway.Response>, t: Throwable) {
                    println(call)
                }
            })
    }
}
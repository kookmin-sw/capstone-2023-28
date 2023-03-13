package com.capstone.traffic.ui.home.route

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentRouteBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.model.network.seoul.locate.Seoul
import com.capstone.traffic.model.network.seoul.SeoulClient
import com.capstone.traffic.model.network.seoul.locate.SeoulService
import com.capstone.traffic.ui.home.HomeActivity
import com.capstone.traffic.ui.home.HomeViewModel
import com.capstone.traffic.ui.home.route.line.LineFragment
import com.capstone.traffic.ui.inform.search.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val binding by lazy { FragmentRouteBinding.inflate(layoutInflater) }
    lateinit var param50 : LinearLayout.LayoutParams
    lateinit var params65 : LinearLayout.LayoutParams
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.searchCv.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putExtra("line", viewModel.selectedLine.value)
            startActivity(intent)
        }
        val sp50 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50f, resources.displayMetrics)
                .toInt()
        val sp65 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 65f, resources.displayMetrics)
                .toInt()
        val sp5 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5f, resources.displayMetrics)
                .toInt()
        param50 = LinearLayout.LayoutParams(sp50, sp50)
        params65 = LinearLayout.LayoutParams(sp65, sp65)

        param50.setMargins(sp5, sp5, sp5, sp5)

        binding.route = viewModel

        viewModel.selectedLine.observe(viewLifecycleOwner, Observer {
            if(MyApplication.status) {
                return@Observer
            }
            MyApplication.status = true
            setImage()
            "${it}".also { binding.tv.text = it;
                val mainColor = getStationColor(it)
                binding.circleCv.backgroundTintList =  ColorStateList.valueOf(mainColor);
                binding.searchCv.backgroundTintList = ColorStateList.valueOf(mainColor);
                binding.startStationImageView.setBackgroundColor(mainColor)
            }
            val ft = childFragmentManager.beginTransaction()
            when (it) {
                1 -> {
                    binding.line1Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("1")
                    ).commit()
                }
                2 -> {
                    binding.line2Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("2")
                    ).commit()
                }
                3 -> {
                    binding.line3Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("3")
                    ).commit()
                }
                4 -> {
                    binding.line4Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("4")
                    ).commit()
                }
                5 -> {
                    binding.line5Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("5")
                    ).commit()
                }
                6 -> {
                    binding.line6Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("6")
                    ).commit()
                }
                7 -> {
                    binding.line7Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("7")
                    ).commit()
                }
                8 -> {
                    binding.line8Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("8")
                    ).commit()
                }
                9 -> {
                    binding.line9Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        LineFragment("9")
                    ).commit()
                }
            }
        })

        // init - 데이터 선언
        //getSeoulApi()

        return binding.root
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
    companion object {
        fun newInstance(title: String) = RouteFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }

    fun setImage()
    {
        binding.line1Btn.layoutParams = param50
        binding.line2Btn.layoutParams = param50
        binding.line3Btn.layoutParams = param50
        binding.line4Btn.layoutParams = param50
        binding.line5Btn.layoutParams = param50
        binding.line6Btn.layoutParams = param50
        binding.line7Btn.layoutParams = param50
        binding.line8Btn.layoutParams = param50
        binding.line9Btn.layoutParams = param50
    }
    fun getSeoulApi(line: Int) {

        val retrofit = SeoulClient.getInstance()
        val seoulService = retrofit.create(SeoulService::class.java)
        seoulService.getInfo("http://swopenapi.seoul.go.kr/api/subway/${APIKEY.SEOUL_APIKEY}/json/realtimePosition/0/1000/${line}호선")
            .enqueue(object : Callback<Seoul> {
                override fun onResponse(call: Call<Seoul>, response: Response<Seoul>) {

                    if (response.isSuccessful) {
                        val data = response.body()?.realtimePositionList
                    }
                }

                override fun onFailure(call: Call<Seoul>, t: Throwable) {
                }
            })

    }
}
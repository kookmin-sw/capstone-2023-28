package com.capstone.traffic.ui.home.route

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentRouteBinding
import com.capstone.traffic.global.appkey.APIKEY
import com.capstone.traffic.model.network.seoul.Seoul
import com.capstone.traffic.model.network.seoul.SeoulClient
import com.capstone.traffic.model.network.seoul.SeoulService
import com.capstone.traffic.ui.home.HomeViewModel
import com.capstone.traffic.ui.home.route.line.Line1Fragment
import com.capstone.traffic.ui.home.route.line.Line2Fragment
import com.capstone.traffic.ui.home.route.line.Line3Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val binding by lazy { FragmentRouteBinding.inflate(layoutInflater) }


    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val title = requireArguments().getString("title")

        val sp50 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50f, resources.displayMetrics)
                .toInt()
        val sp65 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 65f, resources.displayMetrics)
                .toInt()
        val sp5 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5f, resources.displayMetrics)
                .toInt()
        val param50 = LinearLayout.LayoutParams(sp50, sp50)
        val params65 = LinearLayout.LayoutParams(sp65, sp65)

        param50.setMargins(sp5, sp5, sp5, sp5)

        binding.route = viewModel

        viewModel.beforeSelectedLine.observe(viewLifecycleOwner, Observer {
            when (it) {
                1 -> {
                    binding.line1Btn.layoutParams = param50
                }
                2 -> {
                    binding.line2Btn.layoutParams = param50
                }
                3 -> {
                    binding.line3Btn.layoutParams = param50
                }
                4 -> {
                    binding.line4Btn.layoutParams = param50
                }
                5 -> {
                    binding.line5Btn.layoutParams = param50
                }
                6 -> {
                    binding.line6Btn.layoutParams = param50
                }
                7 -> {
                    binding.line7Btn.layoutParams = param50
                }
                8 -> {
                    binding.line8Btn.layoutParams = param50
                }
                9 -> {
                    binding.line9Btn.layoutParams = param50
                }
            }
        })
        viewModel.selectedLine.observe(viewLifecycleOwner, Observer {
            "${it}호선".also { binding.lineTV.text = it }
            val ft = childFragmentManager.beginTransaction()
            when (it) {
                1 -> {
                    binding.line1Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
                2 -> {
                    binding.line2Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line2Fragment()
                    ).commit()
                }
                3 -> {
                    binding.line3Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line3Fragment()
                    ).commit()
                }
                4 -> {
                    binding.line4Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
                5 -> {
                    binding.line5Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
                6 -> {
                    binding.line6Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
                7 -> {
                    binding.line7Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
                8 -> {
                    binding.line8Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
                9 -> {
                    binding.line9Btn.layoutParams = params65; ft.replace(
                        R.id.container,
                        Line1Fragment()
                    ).commit()
                }
            }
        })

        // init - 데이터 선언
        //getSeoulApi()

        return binding.root
    }


    companion object {
        fun newInstance(title: String) = RouteFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
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
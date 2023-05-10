package com.capstone.traffic.ui.inform

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.databinding.FragmentInformBinding
import com.capstone.traffic.model.network.demon.DemonClient
import com.capstone.traffic.model.network.demon.DemonService
import com.capstone.traffic.model.network.demon.Response
import com.capstone.traffic.model.network.seoulMetro.Res
import com.capstone.traffic.model.network.seoulMetro.Service
import com.capstone.traffic.model.network.twitter.Client
import com.capstone.traffic.model.network.twitter.Data
import com.capstone.traffic.model.network.twitter.RankService
import com.capstone.traffic.model.network.twitter.ResponseData
import com.capstone.traffic.ui.inform.demon.Demon
import com.capstone.traffic.ui.inform.demon.DemonAdapter
import com.capstone.traffic.ui.inform.ranking.RankingAdapter
import com.capstone.traffic.ui.inform.twitter.TwitAdapter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback


class InformFragment : Fragment() {

    private val binding by lazy { FragmentInformBinding.inflate(layoutInflater) }
    private var job: Job? = null
    lateinit var DemonData: MutableList<com.capstone.traffic.model.network.demon.Data>
    lateinit var demonAdapter : DemonAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DemonData = mutableListOf()
        demonAdapter = DemonAdapter(requireContext())

        binding.apply {
            getTwitApi()
        }

        // 화면 전환 on off
        job = GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                getDemonApi()
                getRankApi()
            }
        }
        waitTime()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setTwit(data : List<com.capstone.traffic.model.network.seoulMetro.Data>)
    {
        val twitAdapter = TwitAdapter(requireContext())
        twitAdapter.apply {
            datas = data
        }
        binding.seoulTrafficRc.apply {
            this.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL, false)
            adapter = twitAdapter
        }
        twitAdapter.notifyDataSetChanged()
    }

    private fun getTwitApi()
    {
        val retrofit = com.capstone.traffic.model.network.seoulMetro.Client.getInstance()
        val service = retrofit.create(Service::class.java)

        service.getTwit().enqueue(object : Callback<Res>{
            override fun onResponse(call: Call<Res>, response: retrofit2.Response<Res>) {
                if(response.isSuccessful){
                    if(response.body() != null) setTwit(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<Res>, t: Throwable) {
            }
        })
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setDemonRecyclerView()
    {
        if (DemonData.isNotEmpty()) {
            demonAdapter.datas = DemonData
            binding.demonRc.apply {
                this.layoutManager =
                    LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                this.adapter = demonAdapter
                demonAdapter.notifyDataSetChanged()
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setRankRecyclerView(leftData: List<Data>, rightData: List<Data>) {
        val rankAdapter1 = RankingAdapter(requireContext())
        val rankAdapter2 = RankingAdapter(requireContext())
        rankAdapter1.datas = leftData
        rankAdapter2.datas = rightData
        binding.iv1.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            this.adapter = rankAdapter1
        }
        binding.iv2.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            this.adapter = rankAdapter2

        }
        rankAdapter1.notifyDataSetChanged()
        rankAdapter2.notifyDataSetChanged()
    }

    private val rankChangeHandler: Handler by lazy { Handler() }
    private fun waitTime() {
        rankChangeHandler.postDelayed(::changeItem, 3000)
    }

    private fun changeItem() {
        if (binding.iv1.visibility == View.VISIBLE) binding.iv1.visibility = View.GONE
        else {
            //API UPDATE
            //getRankApi()
            binding.iv1.visibility = View.VISIBLE
        }
        waitTime()
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
                        binding.statusTimeTv.text = "${data!![0].date} 기준"
                        DemonData = data as MutableList<com.capstone.traffic.model.network.demon.Data>
                        setDemonRecyclerView()
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                }
            })
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
                        val leftData = data.slice((0..4))
                        val rightData = data.slice((4..data.lastIndex))
                        setRankRecyclerView(leftData, rightData)
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                }
            })
    }

    companion object {
        fun newInstance(title: String) = InformFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
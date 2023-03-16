package com.capstone.traffic.ui.inform

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Range
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.databinding.FragmentInformBinding
import com.capstone.traffic.model.network.twitter.Client
import com.capstone.traffic.model.network.twitter.Data
import com.capstone.traffic.model.network.twitter.RankService
import com.capstone.traffic.model.network.twitter.ResponseData
import com.capstone.traffic.ui.inform.ranking.RankingAdapter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback


class InformFragment : Fragment() {

    private val binding by lazy { FragmentInformBinding.inflate(layoutInflater) }
    private var job : Job? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // 화면 전환 on off
        job = GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                //getRankApi()
            }
        }
        waitTime()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setRankRecyclerView(leftData : List<Data>, rightData : List<Data>){
        val rankAdapter1 = RankingAdapter(requireContext())
        val rankAdapter2 = RankingAdapter(requireContext())
        rankAdapter1.datas = leftData
        rankAdapter2.datas = rightData
        binding.iv1.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            this.adapter = rankAdapter1
        }
        binding.iv2.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            this.adapter = rankAdapter2

        }
        rankAdapter1.notifyDataSetChanged()
        rankAdapter2.notifyDataSetChanged()
    }
    private val rankChangeHandler : Handler by lazy { Handler() }
    private fun waitTime() {
        rankChangeHandler.postDelayed(::changeItem, 3000)
    }
    private fun changeItem(){
        if(binding.iv1.visibility == View.VISIBLE) binding.iv1.visibility = View.GONE
        else{
            //API UPDATE
            //getRankApi()
            binding.iv1.visibility = View.VISIBLE
        }
        waitTime()
    }

    private fun getRankApi() {
        val retrofit = Client.getInstance()
        val service = retrofit.create(RankService::class.java)
        service.getResponse()
            .enqueue(object : Callback<ResponseData> {
                override fun onResponse(call: Call<ResponseData>, response: retrofit2.Response<ResponseData>) {
                    if(response.isSuccessful){
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
                putString("title", title)
            }
        }
    }
}
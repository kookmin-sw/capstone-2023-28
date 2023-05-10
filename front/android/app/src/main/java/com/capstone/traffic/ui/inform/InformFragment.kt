package com.capstone.traffic.ui.inform

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.databinding.FragmentInformBinding
import com.capstone.traffic.model.network.demon.DemonClient
import com.capstone.traffic.model.network.demon.DemonService
import com.capstone.traffic.model.network.demon.Response
import com.capstone.traffic.model.network.news.NewSet
import com.capstone.traffic.model.network.news.News
import com.capstone.traffic.model.network.seoulMetro.Res
import com.capstone.traffic.model.network.seoulMetro.Service
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.twitter.Client
import com.capstone.traffic.model.network.twitter.Data
import com.capstone.traffic.model.network.twitter.RankService
import com.capstone.traffic.model.network.twitter.ResponseData
import com.capstone.traffic.ui.inform.demon.Demon
import com.capstone.traffic.ui.inform.demon.DemonAdapter
import com.capstone.traffic.ui.inform.news.NewsAdapter
import com.capstone.traffic.ui.inform.ranking.RankingAdapter
import com.capstone.traffic.ui.inform.twitter.TwitAdapter
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.create
import kotlin.concurrent.thread


class InformFragment : Fragment() {
    private val viewModel : InformViewModel by viewModels()
    private val binding by lazy { FragmentInformBinding.inflate(layoutInflater) }
    lateinit var DemonData: MutableList<com.capstone.traffic.model.network.demon.Data>
    lateinit var demonAdapter : DemonAdapter

    lateinit var newsData : MutableList<News>
    lateinit var newsAdapter: NewsAdapter
    var status = MutableList(4){false}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        loadingStart()
        DemonData = mutableListOf()
        demonAdapter = DemonAdapter(requireContext())

        newsData = mutableListOf()
        newsAdapter = NewsAdapter(requireContext()){

        }



        binding.apply {
            inform = viewModel
        }

        viewModel.apply {
            demonData.observe(viewLifecycleOwner, Observer {
                DemonData = it
                setDemonRecyclerView()
                status[0] = true
                checkStatus()
            })
            newsData.observe(viewLifecycleOwner, Observer {
                getOGFromURL(it)
                status[1] = true
                checkStatus()
            })
            twitData.observe(viewLifecycleOwner, Observer {
                setRankRecyclerView(it)
                status[2] = true
                checkStatus()
            })
            rankData.observe(viewLifecycleOwner, Observer {
                setTwit(it)
                status[3] = true
                checkStatus()
            })
        }
        return binding.root
    }
    private fun checkStatus() : Boolean
    {
        for(stat in status) {
            if(!stat) return false
        }
        loadingEnd()
        return true
    }
    private fun loadingStart()
    {
        binding.apply {
            parentSV.visibility = View.GONE
            lottieLoading.playAnimation()
            lottieLoading.visibility = View.VISIBLE
        }
    }
    private fun loadingEnd()
    {
        binding.apply {
            parentSV.visibility = View.VISIBLE
            lottieLoading.pauseAnimation()
            lottieLoading.visibility = View.GONE
        }
    }


    private fun getOGFromURL(data : List<News>)
    {
        val newsSetList = mutableListOf<NewSet>()
        for(news in data)
        {
            val url = news.url
            var doc : org.jsoup.nodes.Document? = null
            val thread = Thread{
                doc = Jsoup.connect(url).get()
            }
            thread.start()
            thread.join()
            val ogThread = Thread{
                val ogTitle : String = doc!!.select("meta[property=og:title]").attr("content")
                val ogImage : String = doc!!.select("meta[property=og:image]").attr("content")
                val ogDescription : String = doc!!.select("meta[property=og:description]").attr("content")
                newsSetList.add(NewSet(image = ogImage, title = ogTitle, description = ogDescription))
            }
            ogThread.start()
            ogThread.join()
        }
        setNews(newsSetList)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setNews(data : List<NewSet>)
    {
        newsAdapter.apply {
            datas = data
        }
        binding.newsRc.apply {
            this.layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL, false)
            adapter = newsAdapter
        }
        newsAdapter.notifyDataSetChanged()
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
    private fun setRankRecyclerView(Data: List<Data>) {
        val rankAdapter = RankingAdapter(requireContext())
        rankAdapter.datas = Data
        binding.iv1.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            this.adapter = rankAdapter
        }
        rankAdapter.notifyDataSetChanged()
    }



    companion object {
        fun newInstance(title: String) = InformFragment().apply {
            arguments = Bundle().apply {
            }
        }
    }
}
package com.capstone.traffic.ui.home.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.traffic.databinding.FragmentMainBinding
import com.capstone.traffic.ui.home.HomeActivity
import java.util.*

class MainFragment : Fragment() {
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    var rankIndex = 0
    val rankList = listOf("1호선", "2호선", "3호선", "4호선", "5호선")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val title = requireArguments().getString("title")
        binding.textView.text = title

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        var homeActivity = context as HomeActivity
        var timer = Timer()
        timer.schedule(object: TimerTask(){
            override fun run() {
                homeActivity.runOnUiThread{
                    binding.rankContent.text = rankList[rankIndex]
                }
                rankIndex = (rankIndex+1) % rankList.size
            }
        }, 3000, 3000)
    }
    companion object {
        fun newInstance(title: String) = MainFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }

}

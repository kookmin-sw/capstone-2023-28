package com.capstone.traffic.ui.home.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentMainBinding
import com.capstone.traffic.ui.home.HomeActivity
import java.util.*

class MainFragment : Fragment() {
    var homeActivity: HomeActivity? = null
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }

    //--임시--
    private var rankIndex = 0
    private val rankList = listOf("1호선", "2호선", "3호선", "4호선", "5호선")
    private val feedContents = listOf(Feed("실키보이즈 돌아오기를 기다렸다 앞으로 좋은 음악 만들어주면 좋겠어요..ㅎ", null, "빠꾸", listOf("1호선", "2호선"), "1일전"),
        Feed("실키보이즈 돌아오기를 기다렸다 앞으로 좋은 음악 만들어주면 좋겠어요..ㅎ", null, "빠꾸", listOf("1호선", "2호선"), "1일전"),
        Feed("실키보이즈 돌아오기를 기다렸다 앞으로 좋은 음악 만들어주면 좋겠어요..ㅎ", null, "빠꾸", listOf("1호선", "2호선"), "1일전"),
        Feed("실키보이즈 돌아오기를 기다렸다 앞으로 좋은 음악 만들어주면 좋겠어요..ㅎ", null, "빠꾸", listOf("1호선", "2호선"), "1일전"),
        Feed("실키보이즈 돌아오기를 기다렸다 앞으로 좋은 음악 만들어주면 좋겠어요..ㅎ", null, "빠꾸", listOf("1호선", "2호선"), "1일전"),
        Feed("실키보이즈 돌아오기를 기다렸다 앞으로 좋은 음악 만들어주면 좋겠어요..ㅎ", null, "빠꾸", listOf("1호선", "2호선"), "1일전"),)
    //-------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val title = requireArguments().getString("title")
        //binding.textView.text = title

        // recyclerView 적용

        var feedRecyclerView = binding.feedRecyclerview
        feedRecyclerView.layoutManager = LinearLayoutManager(homeActivity!!, LinearLayoutManager.VERTICAL, false)
        feedRecyclerView.adapter = FeedAdapter(feedContents)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Context 받기
        homeActivity = context as HomeActivity

//        // Timer Schedule을 이용해서 일정 주기마다 전광판 텍스트 교체
//        var timer = Timer()
//        timer.schedule(object: TimerTask(){
//            override fun run() {
//                // runOnUiThread: TimerTask는 메인 스레드가 아니라서 뷰를 바꾸는 행위는 불가능함. 그래서 context를 받아와서 runOnUiThread를 통해 메인 스레드에서 뷰를 바꾸게끔 함.
//                homeActivity!!.runOnUiThread{
//                    binding.rankContent.text = rankList[rankIndex]
//                }
//                rankIndex = (rankIndex+1) % rankList.size
//            }
//        }, 3000, 3000)
    }
    companion object {
        fun newInstance(title: String) = MainFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

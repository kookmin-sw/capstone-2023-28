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
    private val subwayNowContent = listOf("1호선 석계역에서 시위가 예정되어 있습니다.", "경찰청 시위 정보", "안녕하세요")
    private val topicTop3 = listOf(Topic(1, "종각역", "여기 홍수남 ㄷㄷ", 153, 1995), Topic(2, "잠실역", "잠실역 화장실 ㅇㄷ", 35, 195), Topic(1, "종각역", "여기 홍수남 ㄷㄷ", 153, 1995))
    private var bookmarkContent: MutableMap<String, String> = mutableMapOf("1호선 게시판" to "안녕하세요", "2호선 게시판" to "2호선 입니다")
    //-------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val title = requireArguments().getString("title")
        //binding.textView.text = title

        // recyclerView 적용
        var subwayNowRecyclerView = binding.subwayNowRecyclerview
        subwayNowRecyclerView.layoutManager = LinearLayoutManager(homeActivity!!, LinearLayoutManager.HORIZONTAL,false)
        subwayNowRecyclerView.adapter = SubwayNowAdapter(subwayNowContent)

        var topicRecyclerView = binding.topicRecyclerview
        topicRecyclerView.layoutManager = LinearLayoutManager(homeActivity!!, LinearLayoutManager.VERTICAL, false)
        topicRecyclerView.adapter = TopicAdapter(topicTop3)

        var bookmarkRecyclerView = binding.bookmarkRecyclerview
        bookmarkRecyclerView.layoutManager = LinearLayoutManager(homeActivity!!, LinearLayoutManager.VERTICAL, false)
        bookmarkRecyclerView.adapter = BookmarkAdapter(bookmarkContent)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Context 받기
        homeActivity = context as HomeActivity

        // Timer Schedule을 이용해서 일정 주기마다 전광판 텍스트 교체
        var timer = Timer()
        timer.schedule(object: TimerTask(){
            override fun run() {
                // runOnUiThread: TimerTask는 메인 스레드가 아니라서 뷰를 바꾸는 행위는 불가능함. 그래서 context를 받아와서 runOnUiThread를 통해 메인 스레드에서 뷰를 바꾸게끔 함.
                homeActivity!!.runOnUiThread{
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

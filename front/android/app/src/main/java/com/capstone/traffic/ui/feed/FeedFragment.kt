package com.capstone.traffic.ui.feed

import android.app.ProgressDialog.show
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentFeedBinding
import com.capstone.traffic.ui.feed.FeedViewModel.Companion.EVENT_START_FILTER_APPLY
import com.capstone.traffic.ui.feed.FeedViewModel.Companion.EVENT_START_FILTER_SELECT
import com.capstone.traffic.ui.feed.writefeed.WriteFeedActivity
import com.capstone.traffic.ui.my.MyViewModel
import com.google.android.material.card.MaterialCardView


class FeedFragment : Fragment() {

    private val binding by lazy { FragmentFeedBinding.inflate(layoutInflater) }
    private val feedViewModel by activityViewModels<FeedViewModel>()
    private lateinit var lineFilterList : List<AppCompatButton>
    private fun initData(){
        lineFilterList = listOf(
            binding.hs1,
            binding.hs2,
            binding.hs3,
            binding.hs4,
            binding.hs5,
            binding.hs6,
            binding.hs7,
            binding.hs8,
            binding.hs9,
        )
    }

    private var hsList = hs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initData()

        // 뷰 클릭 이벤트 적용
        binding.run {
            filterApplyBtn.setOnClickListener {
                feedViewModel.filterApply()
            }
            lineFilterList.forEach {
                it.setOnClickListener {
                    feedViewModel.filterSelect(it)
                }
            }
            // 스크롤시 새로고침
            refreshLayout.setOnRefreshListener {
                Toast.makeText(requireContext(), "새로고침중", Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false
            }

            // 필터 적용 버튼
            filterApplyBtn.setOnClickListener {
                filterLl.apply { visibility = View.GONE }
            }

            // 필터 초기화
            filterClearBtn.setOnClickListener {
                filterClear()
            }
            //글쓰기 버튼
            writeBtn.setOnClickListener {
                val intent = Intent(requireContext(),WriteFeedActivity::class.java)
                startActivity(intent)
            }

        }

        binding.filterBtn.setOnClickListener {
            binding.filterLl.apply {
                visibility = if(visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        return binding.root
    }

    // 필터 클리어
    private fun filterClear()
    {
        val count = binding.filterGridLayout.childCount
        for(i in 0 until count-1){
            val childView = binding.filterGridLayout.getChildAt(i)
            if(childView is MaterialCardView){
                val lineBtn = childView.getChildAt(0)
                lineBtn?.backgroundTintList = ColorStateList.valueOf(requireContext().resources.getColor(R.color.white))
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel.viewEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    // 필터 적용 버튼 클릭
                    EVENT_START_FILTER_APPLY -> {
                        Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
                    }
                    // 호선 별 필터 버튼 클릭시
                    EVENT_START_FILTER_SELECT -> {
                        //Toast.makeText(context, "select", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        feedViewModel.itemSelectedNum.observe(viewLifecycleOwner, Observer {
            if(it.backgroundTintList != null && it.backgroundTintList!!.equals(ColorStateList.valueOf(requireContext().resources.getColor(R.color.gray)))){
                it.backgroundTintList = ColorStateList.valueOf(requireContext().resources.getColor(R.color.white))
            }
            else it.backgroundTintList = ColorStateList.valueOf(requireContext().resources.getColor(R.color.gray))
        })
    }
    companion object {
        fun newInstance(title: String) = FeedFragment().apply {
        }
    }
}

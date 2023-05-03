package com.capstone.traffic.ui.feed

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentFeedBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.getfeed.FeedResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Res
import com.capstone.traffic.ui.feed.FeedViewModel.Companion.EVENT_START_FILTER_APPLY
import com.capstone.traffic.ui.feed.FeedViewModel.Companion.EVENT_START_FILTER_SELECT
import com.capstone.traffic.ui.feed.feedRC.Feed
import com.capstone.traffic.ui.feed.feedRC.FeedAdapter
import com.capstone.traffic.ui.feed.writefeed.WriteFeedActivity
import com.capstone.traffic.ui.route.direction.SlideUpDialog
import com.google.android.material.card.MaterialCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedFragment : Fragment() {

    private val binding by lazy { FragmentFeedBinding.inflate(layoutInflater) }
    private val feedViewModel by activityViewModels<FeedViewModel>()
    private lateinit var lineFilterList : List<AppCompatButton>
    private lateinit var feedAdapter : FeedAdapter
    private lateinit var contentView : View
    private lateinit var slideUpPopup : SlideUpDialog
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

        val profileData = MyApplication.prefs.getUserProfile()
        binding.myProfileIv.apply {
            if(profileData != null) setBackgroundDrawable(BitmapDrawable(profileData))
        }
    }

    private var hsList = hs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initData()


        // slideview 동적 추가
        contentView = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.dialog_comment,null)
        slideUpPopup = SlideUpDialog.Builder(requireContext())
            .setContentView(contentView)
            .create()

        // 피드 어뎁터 (클릭 이벤트 추가)
        setFeedAdapter()

        initSlider()


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
                retrofitFeed()
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

        retrofitFeed()

        return binding.root
    }


    private fun initSlider()
    {
        // slider 관련 이벤트 정리
        val cancelBtn = contentView.findViewById<AppCompatImageButton>(R.id.cancle_btn)
        val addCommentBtn = contentView.findViewById<AppCompatButton>(R.id.add_comment_btn)
        val inputLL = contentView.findViewById<LinearLayout>(R.id.inputLL)
        val inputTextBtn = contentView.findViewById<EditText>(R.id.input_text_btn)
        val sendBtn = contentView.findViewById<AppCompatButton>(R.id.send_btn)

        // 삭제 버튼 클릭시 사라지기
        cancelBtn.setOnClickListener {
            slideUpPopup.dismissAnim()
        }

        addCommentBtn.setOnClickListener {
            inputLL.visibility = View.VISIBLE
            inputTextBtn.requestFocus()
            keyboardUp()
        }

        sendBtn.setOnClickListener{
            keyboardDown()
        }



    }
    // 키보드 올리기
    private fun keyboardUp()
    {
        val imm : InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    // 키보드 내리기
    private fun keyboardDown()
    {
        val imm : InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
    // 피드 어뎁터 설정
    private fun setFeedAdapter()
    {
        feedAdapter = FeedAdapter(requireContext()){
                item -> Toast.makeText(requireContext(), "${item.user.userNickname}", Toast.LENGTH_SHORT).show()
            slideUpPopup.show()

        }
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

    // sql 피드 불러오기
    private fun retrofitFeed(){
        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        service.getFeed().enqueue(object : Callback<FeedResSuc>{
            override fun onResponse(call: Call<FeedResSuc>, response: Response<FeedResSuc>) {
                if(response.isSuccessful)
                {
                    val data = response.body()?.res
                    if(data != null)setFeedRecyclerView(data)
                }
            }
            override fun onFailure(call: Call<FeedResSuc>, t: Throwable) {
                print(2)
            }
        }
        )
    }

    // recyclerView
    @SuppressLint("NotifyDataSetChanged")
    private fun setFeedRecyclerView(feed : List<Res>)
    {
        feedAdapter.apply {
            datas = feed
        }

        binding.feedRc.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            this.adapter = feedAdapter
        }
        feedAdapter.notifyDataSetChanged()
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

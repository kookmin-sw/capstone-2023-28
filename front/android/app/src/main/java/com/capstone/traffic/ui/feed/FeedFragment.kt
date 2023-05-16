package com.capstone.traffic.ui.feed

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import android.util.Log
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
import androidx.core.content.ContextCompat.getColorStateList
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.text.set
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
import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import com.capstone.traffic.model.network.sql.dataclass.comment.ComResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.FeedResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Res
import com.capstone.traffic.model.network.sql.dataclass.postfeed.res
import com.capstone.traffic.ui.feed.FeedViewModel.Companion.EVENT_START_FILTER_APPLY
import com.capstone.traffic.ui.feed.FeedViewModel.Companion.EVENT_START_FILTER_SELECT
import com.capstone.traffic.ui.feed.comment.CommentAdapter
import com.capstone.traffic.ui.feed.feedRC.Feed
import com.capstone.traffic.ui.feed.feedRC.FeedAdapter
import com.capstone.traffic.ui.feed.find.FindActivity
import com.capstone.traffic.ui.feed.writefeed.WriteFeedActivity
import com.capstone.traffic.ui.profile.ProfileActivity
import com.capstone.traffic.ui.route.direction.SlideUpDialog
import com.google.android.material.card.MaterialCardView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class FeedFragment : Fragment() {

    private val binding by lazy { FragmentFeedBinding.inflate(layoutInflater) }
    private val feedViewModel by activityViewModels<FeedViewModel>()
    private lateinit var lineFilterList : List<AppCompatButton>
    private lateinit var feedAdapter : FeedAdapter
    private lateinit var contentView : View
    private lateinit var slideUpPopup : SlideUpDialog
    private lateinit var selectedFeedId : String
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var feedData: MutableList<Res>
    private var page = 0
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

        feedData = mutableListOf()


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

            // 유저 검새 버튼
            findBtn.setOnClickListener {
                val intent = Intent(requireContext(), FindActivity::class.java)
                startActivity(intent)
            }

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
                refresh()
                val hashTag = getFilterTag()
                retrofitFeed(hashTag, null)
                binding.refreshLayout.isRefreshing = false

            }


            // 필터 초기화
            filterClearBtn.setOnClickListener {
                filterClear()
            }

            //글쓰기 버튼
            writeBtn.setOnClickListener {
                val intent = Intent(requireContext(),WriteFeedActivity::class.java)
                startActivityForResult(intent,1)
            }



            // 페이징
            feedSv.setOnScrollChangeListener { v, _, scrollY, _, _ ->
                if (scrollY == feedSv.getChildAt(0).measuredHeight - v.measuredHeight){
                    val filterData = getFilterTag()
                    retrofitFeed(filterData, null)
                }
            }

        }


        binding.filterBtn.setOnClickListener {

            binding.filterLl.apply {
                visibility = if(visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }

        retrofitFeed(null, null)

        return binding.root
    }
    private fun refresh(){
        page = 0
        feedData = mutableListOf()
    }
    private fun getFilterTag() : String {
        val filterDataList = mutableListOf<Int>()
        // 필터 적용
        lineFilterList.forEachIndexed { index,  it ->
            if(it.backgroundTintList != null && it.backgroundTintList!!.equals(ColorStateList.valueOf(requireContext().resources.getColor(R.color.gray)))){
                filterDataList.add(index + 1)
            }
        }
        val filterString = filterDataList.joinToString(",")
        return filterString
    }
    private fun initSlider()
    {
        commentAdapter = CommentAdapter(requireContext())

        // slider 관련 이벤트 정리
        val cancelBtn = contentView.findViewById<AppCompatImageButton>(R.id.cancle_btn)
        val addCommentBtn = contentView.findViewById<AppCompatButton>(R.id.add_comment_btn)
        val inputLL = contentView.findViewById<LinearLayout>(R.id.inputLL)
        val inputTextBtn = contentView.findViewById<EditText>(R.id.input_text_btn)
        val sendBtn = contentView.findViewById<AppCompatButton>(R.id.send_btn)
        val rc = contentView.findViewById<RecyclerView>(R.id.comment_rc)

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

        sendBtn.setOnClickListener {
            if(selectedFeedId.isNotEmpty() && inputTextBtn.text.isNotBlank()) {
                writeComments(inputTextBtn.text.toString(), selectedFeedId)
            }
        }

        rc.apply {
            this.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
            this.adapter = commentAdapter
        }

    }

    // 댓글 달기
    private fun writeComments(comments : String, feedId: String)
    {
        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val param =
            RequestBody.create(mediaType, "{\"feed_id\":\"${feedId}\",\"content\":\"${comments}\"}")
        service.uploadComments(param = param).enqueue(object : Callback<ImageUpload>{
            override fun onResponse(call: Call<ImageUpload>, response: Response<ImageUpload>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(), "댓글을 작성하였습니다.", Toast.LENGTH_SHORT).show()
                    getComments(selectedFeedId)
                }
                else{
                    Toast.makeText(requireContext(), "댓글을 작성실패.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ImageUpload>, t: Throwable) {
                Toast.makeText(requireContext(), "댓글을 작성실패.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == 3131){
                feedData = mutableListOf()
                page = 0
                filterClear()
                retrofitFeed(null, null)
            }
        }
    }

    // 댓글 가져오기
    private fun getComments(feedId : String)
    {
        val service = Client.getInstance().create(Service::class.java)
        service.getComments(feedId).enqueue(object :Callback<ComResSuc>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ComResSuc>, response: Response<ComResSuc>) {
                if (response.isSuccessful && response.body() != null){
                    commentAdapter.datas = response.body()!!.res
                    commentAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ComResSuc>, t: Throwable) {
            }
        })
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
        feedAdapter = FeedAdapter(
            context = requireContext(),
            // 해당 유저 프로필로 이동
            informClickEvent = {
                val profileIntent = Intent(context, ProfileActivity::class.java)
                profileIntent.putExtra("userName", it.user?.userNickname)
                startActivity(profileIntent)
            },
            // 댓글 창
            onClickListener = {
                selectedFeedId = it.feedId
                getComments(selectedFeedId)
                contentView.findViewById<EditText>(R.id.input_text_btn).setText("")
                keyboardDown()
                slideUpPopup.show()
            },
            deleteListener = {
                deleteDialog(it.feedId)
            }
        )

    }
    private fun deleteDialog(feedId : String) {
        val dialog: AlertDialog = this.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.apply {
                this.setMessage("해당 피드를 삭제하시겠습니까?")
                this.setCancelable(false)
                this.setPositiveButton("삭제") { dialog, _ ->
                    dialog.dismiss()
                    deleteFeed(feedId)
                }
                this.setNegativeButton("취소") { dialog, _ ->
                    dialog.cancel()
                }
            }
            builder.create()
        }
        dialog.show()
    }

    private fun deleteFeed(feedId : String)
    {
        val service = Client.getInstance().create(Service::class.java)

        val mediaType = "application/json".toMediaTypeOrNull()
        val param =
            RequestBody.create(mediaType, "{\"feed_id\":\"${feedId}\"}")

        service.deleteFeed(param = param).enqueue(object :Callback<DefaultRes>{
            override fun onResponse(call: Call<DefaultRes>, response: Response<DefaultRes>) {
                refresh()
                val hashTag = getFilterTag()
                retrofitFeed(hashTag, null)
            }

            override fun onFailure(call: Call<DefaultRes>, t: Throwable) {

            }
        })
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
    private fun retrofitFeed(hashTag : String?, userId : String?){
        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        service.getFeed(hashTag = hashTag, userId, null,"5",page++.toString()).enqueue(object : Callback<FeedResSuc>{
            override fun onResponse(call: Call<FeedResSuc>, response: Response<FeedResSuc>) {
                if(response.isSuccessful)
                {
                    val data = response.body()?.res
                    if(data != null && data.isNotEmpty())
                    {
                        feedData.addAll(data)
                        setFeedRecyclerView()
                    }
                    else{
                        if(hashTag != null && hashTag.isNotEmpty()){
                            setFeedRecyclerView()
                        }
                        else --page
                    }
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
    private fun setFeedRecyclerView()
    {
        feedAdapter.apply {
            datas = feedData
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
                        binding.filterLl.apply { visibility = View.GONE }
                        val filterData = getFilterTag()
                        refresh()
                        retrofitFeed(hashTag = filterData,null)

                    }
                    // 호선 별 필터 버튼 클릭시
                    EVENT_START_FILTER_SELECT -> {
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

        feedViewModel.profile.observe(viewLifecycleOwner, Observer {
            binding.myProfileIv.apply {
                if(it != null) setBackgroundDrawable(BitmapDrawable(it.stringToBitmap()))
            }
        })
    }
    private fun String.stringToBitmap() : Bitmap {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }

    companion object {
        fun newInstance(title: String) = FeedFragment().apply {
        }
    }
}

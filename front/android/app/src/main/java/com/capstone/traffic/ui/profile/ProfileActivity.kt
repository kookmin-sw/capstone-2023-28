package com.capstone.traffic.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityProfileBinding
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import com.capstone.traffic.model.network.sql.dataclass.comment.ComResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Res
import com.capstone.traffic.ui.feed.comment.CommentAdapter
import com.capstone.traffic.ui.feed.feedRC.FeedAdapter
import com.capstone.traffic.ui.my.MyViewModel
import com.capstone.traffic.ui.route.direction.SlideUpDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    private val profileViewModel : ProfileViewModel by viewModels()
    private lateinit var feedAdapter : FeedAdapter
    private val bind by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    private lateinit var contentView : View
    private lateinit var slideUpPopup : SlideUpDialog
    private lateinit var selectedFeedId : String
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var feedDatas : MutableList<Res>
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        feedDatas = mutableListOf()
        feedAdapter = FeedAdapter(
            context = applicationContext,
            informClickEvent = {

            },
            onClickListener = {
                selectedFeedId = it.feedId
                getComments(selectedFeedId)
                contentView.findViewById<EditText>(R.id.input_text_btn).setText("")
                keyboardDown()
                slideUpPopup.show()
            }
        )

        val userNickName = intent.getStringExtra("userName")
        bind.profile = profileViewModel

        profileViewModel.getUserInfo(userNickName!!, page++.toString())

        bind.apply {
            // 페이징
            feedSv.setOnScrollChangeListener { v, _, scrollY, _, _ ->
                if (scrollY == feedSv.getChildAt(0).measuredHeight - v.measuredHeight){
                    profileViewModel.getUserInfo(userNickName, page++.toString())
                }
            }

            followingBtn.setOnClickListener {
                if(profileViewModel.isFollowing.value!!){
                    makeUnFollowing()
                }
                else{
                    makeFollowing()
                }
            }
        }

        // slideview 동적 추가
        contentView = (applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.dialog_comment,null)
        slideUpPopup = SlideUpDialog.Builder(this)
            .setContentView(contentView)
            .create()

        initSlider()


        profileViewModel.apply {
            this.userDefinition.observe(this@ProfileActivity, Observer {
                bind.userMemoTv.text = it
            })
            this.userNickname.observe(this@ProfileActivity, Observer {
                bind.nicknameTv1.text = it
                bind.nicknameTv2.text = it
            })
            this.userProfile.observe(this@ProfileActivity, Observer {
                if(it != null)
                {
                    Glide.with(this@ProfileActivity)
                        .load(it.stringToBitmap())
                        .centerCrop()
                        .into(bind.profileIV)
                }
            })
            this.feedData.observe(this@ProfileActivity, Observer {
                feedDatas.addAll(it)
                setFeedRecyclerView()
                bind.postTv.text = it.size.toString()
            })
            follower.observe(this@ProfileActivity){
                bind.followerTv.text = it.toString()
            }
            following.observe(this@ProfileActivity){
                bind.followingTv.text = it.toString()
            }
            isFollowing.observe(this@ProfileActivity){
                if(it) {
                    bind.followingBtn.text = "언팔로잉"
                }
                else {
                    bind.followingBtn.text = "팔로잉"
                }
            }
        }

        bind.apply {
            // refresh 기능
            refreshLayout.setOnRefreshListener {
                refreshLayout.isRefreshing = false
            }

        }
    }
    private fun makeFollowing()
    {
        val service = Client.getInstance().create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val param =
            RequestBody.create(mediaType, "{\"following_user_id\":\"${profileViewModel.id.value}\"}")
        service.makeFollowing(param = param).enqueue(object :Callback<DefaultRes>{
            override fun onResponse(call: Call<DefaultRes>, response: Response<DefaultRes>) {
                if(response.isSuccessful){
                    profileViewModel.getUserInfo(userName = null, pageIndex = "1", updateFeed = false)
                }
            }

            override fun onFailure(call: Call<DefaultRes>, t: Throwable) {

            }
        })
    }
    private fun makeUnFollowing()
    {
        val service = Client.getInstance().create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val param =
            RequestBody.create(mediaType, "{\"following_user_id\":\"${profileViewModel.id.value}\"}")
        service.makeUnfollowing(param = param).enqueue(object :Callback<DefaultRes>{
            override fun onResponse(call: Call<DefaultRes>, response: Response<DefaultRes>) {
                if(response.isSuccessful){
                    profileViewModel.getUserInfo(userName = null, pageIndex = "1", updateFeed = false)
                }
            }

            override fun onFailure(call: Call<DefaultRes>, t: Throwable) {

            }
        })
    }

    // 키보드 올리기
    private fun keyboardUp()
    {
        val imm : InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun getComments(feedId : String)
    {
        val service = Client.getInstance().create(Service::class.java)
        service.getComments(feedId).enqueue(object : Callback<ComResSuc> {
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

    // 댓글 달기
    private fun writeComments(comments : String, feedId: String)
    {
        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val param =
            RequestBody.create(mediaType, "{\"feed_id\":\"${feedId}\",\"content\":\"${comments}\"}")
        service.uploadComments(param = param).enqueue(object : Callback<ImageUpload> {
            override fun onResponse(call: Call<ImageUpload>, response: Response<ImageUpload>) {
                if(response.isSuccessful){
                    Toast.makeText(applicationContext, "댓글을 작성하였습니다.", Toast.LENGTH_SHORT).show()
                    getComments(selectedFeedId)
                }
                else{
                    Toast.makeText(applicationContext, "댓글을 작성실패.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ImageUpload>, t: Throwable) {
                Toast.makeText(applicationContext, "댓글을 작성실패.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 키보드 내리기
    private fun keyboardDown()
    {
        val imm : InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun initSlider()
    {
        commentAdapter = CommentAdapter(applicationContext)

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
            this.layoutManager = LinearLayoutManager(this@ProfileActivity, RecyclerView.VERTICAL,false)
            this.adapter = commentAdapter
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setFeedRecyclerView()
    {
        feedAdapter.apply {
            datas = feedDatas
        }

        bind.feedRc.apply {
            this.layoutManager = LinearLayoutManager(this@ProfileActivity, RecyclerView.VERTICAL,false)
            this.adapter = feedAdapter
        }
        feedAdapter.notifyDataSetChanged()
    }


    private fun String.stringToBitmap() : Bitmap {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}
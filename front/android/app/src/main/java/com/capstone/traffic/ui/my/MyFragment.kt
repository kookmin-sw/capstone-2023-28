package com.capstone.traffic.ui.my

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentMyBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import com.capstone.traffic.model.network.sql.dataclass.comment.ComResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.FeedResSuc
import com.capstone.traffic.model.network.sql.dataclass.getfeed.Res
import com.capstone.traffic.ui.feed.comment.CommentAdapter
import com.capstone.traffic.ui.feed.writefeed.WriteFeedActivity
import com.capstone.traffic.ui.my.editProfile.EditProfileActivity
import com.capstone.traffic.ui.route.direction.SlideUpDialog
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import rx.Observer
import java.io.File

class MyFragment : Fragment() {
    private val binding by lazy { FragmentMyBinding.inflate(layoutInflater) }
    private val MyViewModel: MyViewModel by viewModels()
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var contentView : View
    private lateinit var slideUpPopup : SlideUpDialog
    private lateinit var selectedFeedId : String
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // slideview 동적 추가
        contentView = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.dialog_comment,null)
        slideUpPopup = SlideUpDialog.Builder(requireContext())
            .setContentView(contentView)
            .create()

        initSlider()
        setProfileBtn()

        // 피드 어뎁터 (클릭 이벤트 추가)
        feedAdapter = FeedAdapter(requireContext()){

        }
        MyViewModel.apply {
            nickname.observe( viewLifecycleOwner, androidx.lifecycle.Observer {
                getMyFeed(it)
                binding.nicknameTv2.text = it
            })
        }

        binding.apply {
            // 스크롤시 새로고침
            refreshLayout.setOnRefreshListener {
                // 프로필 새롭게 가져오기
                profileIV.apply {
                    setProfileBtn()
                }
                getMyFeed(MyViewModel.nickname.value.toString())
                // 내 피드 새로 고침
                refreshLayout.isRefreshing = false

            }

            changeInformBtn.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivityForResult(intent,999)
            }

            feedRc.apply {

            }
        }
        return binding.root
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

    private fun setFeedAdapter()
    {
        feedAdapter = FeedAdapter(requireContext()){
                item -> Toast.makeText(requireContext(), "${item.user?.userNickname}", Toast.LENGTH_SHORT).show()
            selectedFeedId = item.feedId
            getComments(selectedFeedId)
            contentView.findViewById<EditText>(R.id.input_text_btn).setText("")
            keyboardDown()
            slideUpPopup.show()
        }
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

    // 내 피드 가져오기
    private fun getMyFeed(nickname: String)
    {
        setFeedAdapter()

        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        service.getFeed(userNickname = nickname, userId = null, hashTag = null).enqueue(object :
            Callback<FeedResSuc>{
            override fun onResponse(call: Call<FeedResSuc>, response: Response<FeedResSuc>) {
                if(response.isSuccessful)
                {
                    val data = response.body()?.res
                    if(data != null) setFeedRecyclerView(data)
                }
            }

            override fun onFailure(call: Call<FeedResSuc>, t: Throwable) {

            }
            })
    }
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


    private fun setProfileBtn()
    {
        binding.profileIV.apply {
            this.setOnClickListener {
                openGallery()
            }
            val profileData = MyApplication.prefs.getUserProfile()
            if(profileData != null) setBackgroundDrawable(BitmapDrawable(profileData))
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        imageResult.launch(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            CropImage.activity(result.data?.data).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true).start(requireContext(), this)
        }
    }

    // 업데이트 된 프로필 서버에 업로드
    private fun updateProfile(uri : Uri)
    {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), uri.toFile())
        val body = MultipartBody.Part.createFormData("image", "profile", requestFile)

        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        service.updateProfile(body).enqueue(object : retrofit2.Callback<DefaultRes> {
            override fun onResponse(call: Call<DefaultRes>, response: Response<DefaultRes>) {
                if (response.isSuccessful){
                    Toast.makeText(requireContext(), "프로필 사진이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DefaultRes>, t: Throwable) {

            }
        })
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)
            if(resultCode == RESULT_OK){
                val resultUri = result.uri
                binding.profileIV.let {
                    Glide.with(this)
                        .asBitmap()
                        .load(resultUri)
                        .into(binding.profileIV)
                }

                updateProfile(resultUri)
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {}
        }
        else if(requestCode == 999){
            if(resultCode == 9999){
                MyViewModel.getUserAPI()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyViewModel.follower.observe(viewLifecycleOwner){
            binding.followerTv.text = it.toString()
        }
        MyViewModel.post.observe(viewLifecycleOwner){
            binding.postTv.text = it.toString()
        }
        MyViewModel.following.observe(viewLifecycleOwner){
            binding.followingTv.text = it.toString()
        }
        MyViewModel.nickname.observe(viewLifecycleOwner){
            binding.nicknameTv1.text = it
        }
        MyViewModel.userDefini.observe(viewLifecycleOwner){
            binding.userMemoTv.text = it
        }
    }
    companion object {
        fun newInstance(title: String) = MyFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

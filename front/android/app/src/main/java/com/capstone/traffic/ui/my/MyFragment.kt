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
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.traffic.databinding.FragmentMyBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.ui.feed.writefeed.WriteFeedActivity
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
import retrofit2.Response
import java.io.File

class MyFragment : Fragment() {
    private val binding by lazy { FragmentMyBinding.inflate(layoutInflater) }
    private val MyViewModel: MyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setProfileBtn()
        binding.apply {
            // 스크롤시 새로고침
            refreshLayout.setOnRefreshListener {
                // 프로필 새롭게 가져오기
                profileIV.apply {
                    setProfileBtn()
                }
                // 내 피드 새로 고침
                refreshLayout.isRefreshing = false
            }
        }

        return binding.root
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
    }
    companion object {
        fun newInstance(title: String) = MyFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

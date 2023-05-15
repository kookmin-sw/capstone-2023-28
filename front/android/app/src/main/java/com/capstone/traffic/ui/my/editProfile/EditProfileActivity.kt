package com.capstone.traffic.ui.my.editProfile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.IDNA.Info
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityEditProfileBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import com.capstone.traffic.ui.my.MyViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
    var userUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getUserData()
        binding.apply {
            cancleBtn.setOnClickListener {
                finish()
            }
            endBtn.setOnClickListener {
                // 사진 업로드
                if(defiEt.text.isBlank() || nameEt.text.isBlank()){
                    Toast.makeText(this@EditProfileActivity, "닉네임과 소개는 공백이 될 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    upDateProfile()

                }
            }
            editProfile.setOnClickListener {
                openGallery()
            }
        }
    }

    // 종료
    private fun end()
    {
        val intent = Intent()
        setResult(9999,intent)
        finish()
    }
    private fun upDateProfile(){
        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)

        val mediaType = "application/json".toMediaTypeOrNull()
        val param =
            RequestBody.create(mediaType, "{\"user_nickname\":\"${binding.nameEt.text}\",\"user_definition\":\"${binding.defiEt.text ?: " "}\"}")

        service.updateProfile(param).enqueue(object : Callback<DefaultRes>{
            override fun onResponse(call: Call<DefaultRes>, response: Response<DefaultRes>) {
                if(response.isSuccessful){
                    when(response.body()?.status){
                        // 성공
                        "OK" -> {
                            updateProfile(userUri)
                        }
                        // 실패
                        "ERROR" -> {
                            Toast.makeText(this@EditProfileActivity, response.body()?.res?.errorName, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DefaultRes>, t: Throwable) {

            }
        })
    }
    private fun getUserData(){
        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)

        service.getInfo(userEmail = MyApplication.prefs.getEmail(), userNickname = null, pageNum = null).enqueue(object :
            Callback<InfoRecSuc>{
            override fun onResponse(call: Call<InfoRecSuc>, response: Response<InfoRecSuc>) {
                if(response.isSuccessful && response.body()?.res!!.isNotEmpty()){
                    if(response.body()?.res!![0].user_profile_image != null)binding.profileIv.setBackgroundDrawable(BitmapDrawable(response.body()?.res!![0].user_profile_image!!.stringToBitmap()))
                    binding.nameEt.setText(response.body()?.res!![0].userNickName)
                    binding.defiEt.setText(response.body()?.res!![0].userDefinition)
                }
            }

            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {

            }
            })
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)
            if(resultCode == RESULT_OK){
                userUri = result.uri
                binding.profileIv.let {
                    Glide.with(this)
                        .asBitmap()
                        .load(userUri)
                        .into(binding.profileIv)
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {}
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
            CropImage.activity(result.data?.data).setCropShape(CropImageView.CropShape.OVAL).setFixAspectRatio(true).start(
               this)
        }
    }
    // 업데이트 된 프로필 서버에 업로드
    private fun updateProfile(uri : Uri?)
    {
        if(uri == null) {
            end()
            return
        }
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), uri!!.toFile())
        val body = MultipartBody.Part.createFormData("image", "profile", requestFile)

        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)
        service.updateProfile(body).enqueue(object : retrofit2.Callback<DefaultRes> {
            override fun onResponse(call: Call<DefaultRes>, response: Response<DefaultRes>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "프로필이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                    end()

                }
            }

            override fun onFailure(call: Call<DefaultRes>, t: Throwable) {

            }
        })
    }

    private fun String.stringToBitmap() : Bitmap? {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}
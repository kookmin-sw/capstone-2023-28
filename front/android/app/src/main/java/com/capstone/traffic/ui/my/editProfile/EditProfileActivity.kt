package com.capstone.traffic.ui.my.editProfile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.IDNA.Info
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityEditProfileBinding
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.DefaultRes
import com.capstone.traffic.model.network.sql.dataclass.info.InfoRecSuc
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
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
                else upDateProfile()
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
                            end()
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
                if(response.isSuccessful){
                    binding.profileIv.setBackgroundDrawable(BitmapDrawable(response.body()?.res?.user_profile_image?.stringToBitmap()))
                    binding.nameEt.setText(response.body()?.res?.userNickName)
                    binding.defiEt.setText(response.body()?.res?.userDefinition)
                }
            }

            override fun onFailure(call: Call<InfoRecSuc>, t: Throwable) {

            }
            })
    }
    private fun String.stringToBitmap() : Bitmap {
        val encodeByte = android.util.Base64.decode(this, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}
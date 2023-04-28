package com.capstone.traffic.ui.feed.writefeed

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityWriteFeedBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.model.network.sk.direction.dataClass.name
import com.capstone.traffic.model.network.sk.direction.dataClass.testData.data
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import com.capstone.traffic.model.network.sql.dataclass.login.LoginResFail
import com.capstone.traffic.model.network.sql.dataclass.login.LoginResSuc
import com.capstone.traffic.model.network.sql.dataclass.postfeed.response
import com.capstone.traffic.ui.feed.writefeed.addImage.Image
import com.capstone.traffic.ui.feed.writefeed.addImage.ListAdapterGrid
import com.capstone.traffic.ui.feed.writefeed.addImage.Status
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
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

class WriteFeedActivity : BaseActivity<ActivityWriteFeedBinding>() {
    override var layoutResourceId: Int = R.layout.activity_write_feed
    private lateinit var writeFeedViewModel: WriteFeedViewModel
    private var userId = -1

    override fun initBinding() {

        writeFeedViewModel = ViewModelProvider(this)[WriteFeedViewModel::class.java]
        binding.write = writeFeedViewModel

        binding.run {
            backBtn.setOnClickListener {
                cancelPosting()
            }
            postBtn.setOnClickListener {
                // 작성된 글이 없으면 업로드 불가
                if (binding.postingEt.text.isBlank()) Toast.makeText(
                    this@WriteFeedActivity,
                    "피드를 작성해주세요",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    posting()
                    if (photoList.isNotEmpty()) {

                    }
                }
            }
            takePictureBtn.setOnClickListener {
                requestPermission(CAMERA_PERMISSION)
            }

            // 사진 가져오기 기능
            addPictureBtn.setOnClickListener {
                requestPermission(GALLERY_PERMISSION)
            }
            // 사진 리사이클러 뷰
            var listAdapter = ListAdapterGrid(photoList,
                onClickDeleteIcon = {
                    // 사진 삭제 기능
                    deletePhoto(it)
                }
            )
            rcv.adapter = listAdapter
            rcv.layoutManager = GridLayoutManager(this@WriteFeedActivity, 3)

        }

    }

    private fun uploadingPhoto() {
        val retrofit = Client.getInstance()
        val photoService = retrofit.create(Service::class.java)
        val data = photoList[0].uri?.asMultipart(contentResolver)
        val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(),userId.toString())
        if (data != null) {
            photoService.uploadImage(userIdBody,data).enqueue(object : Callback<ImageUpload> {
                override fun onResponse(call: Call<ImageUpload>, response: Response<ImageUpload>) {
                    print(1)
                }

                override fun onFailure(call: Call<ImageUpload>, t: Throwable) {
                    print(2)
                    TODO("Not yet implemented")
                }
            }
            )
        }
    }

    fun Uri.asMultipart(contentResolver: ContentResolver): MultipartBody.Part? {
        return contentResolver.query(this, null, null, null, null)?.let {
            if (it.moveToNext()) {
                val displayName = "feed.jpg"
                val requestBody = object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return contentResolver.getType(this@asMultipart)?.toMediaType()
                    }

                    override fun writeTo(sink: BufferedSink) {
                        sink.writeAll(contentResolver.openInputStream(this@asMultipart)?.source()!!)
                    }
                }
                it.close()
                MultipartBody.Part.createFormData("image", displayName, requestBody)
            } else {
                it.close()
                null
            }
        }
    }

    private fun cancelPosting() {
        finish()
    }

    // 게시 완료
    private fun posting() {
        // 사진이 없는 경우 -> 글만 업로드
        if (photoList.size == 0) {
            postingText()
        }
        else{
            postingText()
            uploadingPhoto()
        }
        cancelPosting()
    }

    // 피드 업로드 (텍스트 만)
    private fun postingText() {
        val retrofit = Client.getInstance()
        val loginService = retrofit.create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val postingText = binding.postingEt.text
        val param =
            RequestBody.create(mediaType, "{\"content\":\"${postingText}\",\"hash_tags\":[]}")
        loginService.getPostingText(param = param)
            .enqueue(object : Callback<response> {
                override fun onResponse(call: Call<response>, response: Response<response>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@WriteFeedActivity, "업로드 완료", Toast.LENGTH_SHORT).show()
                        userId = response.body()?.res?.feedid?.toInt() ?: -1
                    }
                }

                override fun onFailure(call: Call<response>, t: Throwable) {
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePhoto(status: Status) {
        photoList.removeAt(status.position)
        binding.rcv.adapter?.notifyDataSetChanged()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        imageResult.launch(intent)
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val values = ContentValues()
        camUri = this@WriteFeedActivity.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        intent.putExtra(MediaStore.EXTRA_OUTPUT, camUri)
        imageResult.launch(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // 이미지를 받으면 ImageView에 적용한다
            if (result.data?.data == null) photoList.add(Image(camUri))
            else photoList.add(Image(result.data?.data))
            binding.rcv.adapter?.notifyDataSetChanged()
        }
    }

    private fun requestPermission(requestPermission: Int) {
        when (requestPermission) {
            CAMERA_PERMISSION -> {
                TedPermission.create()
                    .setPermissionListener(object : PermissionListener {

                        //권한이 허용됐을 때
                        override fun onPermissionGranted() {
                            openCamera()
                        }

                        //권한이 거부됐을 때
                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        }
                    })
                    .setDeniedMessage("카메라 권한을 허용해주세요.")
                    .setPermissions(android.Manifest.permission.CAMERA)
                    .check()
            }
            GALLERY_PERMISSION -> {
                TedPermission.create()
                    .setPermissionListener(object : PermissionListener {

                        //권한이 허용됐을 때
                        override fun onPermissionGranted() {
                            openGallery()
                        }

                        //권한이 거부됐을 때
                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        }
                    })
                    .setDeniedMessage("갤러리 권한을 허용해주세요.")
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .check()
            }
        }
    }

    companion object {
        const val CAMERA_PERMISSION = 0
        const val GALLERY_PERMISSION = 1
        lateinit var camUri: Uri
        private var photoList = arrayListOf<Image>()
    }
}
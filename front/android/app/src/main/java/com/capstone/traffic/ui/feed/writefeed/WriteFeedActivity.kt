package com.capstone.traffic.ui.feed.writefeed

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityWriteFeedBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.model.network.sql.Client
import com.capstone.traffic.model.network.sql.Service
import com.capstone.traffic.model.network.sql.dataclass.ImageUpload
import com.capstone.traffic.model.network.sql.dataclass.postfeed.response
import com.capstone.traffic.ui.feed.writefeed.addImage.Image
import com.capstone.traffic.ui.feed.writefeed.addImage.ListAdapterGrid
import com.capstone.traffic.ui.feed.writefeed.addImage.Status
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class WriteFeedActivity : BaseActivity<ActivityWriteFeedBinding>() {
    override var layoutResourceId: Int = R.layout.activity_write_feed
    private lateinit var writeFeedViewModel: WriteFeedViewModel
    private var userId = -1
    private lateinit var lineFilterList : List<AppCompatButton>
    var fileAbsolutePath: String? = null
    private fun initFilterBtn()
    {
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
        lineFilterList.forEach {
            it.setOnClickListener {
                if(it.backgroundTintList != null && it.backgroundTintList!!.equals(ColorStateList.valueOf(
                        this.resources.getColor(R.color.gray)))){
                    it.backgroundTintList = ColorStateList.valueOf(this.resources.getColor(R.color.white))
                }
                else it.backgroundTintList = ColorStateList.valueOf(this.resources.getColor(R.color.gray))
            }
        }
    }


    override fun initBinding() {

        // 필터 버튼 초기화
        initFilterBtn()

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
    private fun getRealPathFromURI(contentUri: Uri): String? {
        if (contentUri.path!!.startsWith("/storage")) {
            return contentUri.path
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        val columns = arrayOf(MediaStore.Files.FileColumns.DATA)
        val selection = MediaStore.Files.FileColumns._ID + " = " + id
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            columns,
            selection,
            null,
            null
        )
        try {
            val columnIndex = cursor!!.getColumnIndex(columns[0])
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor!!.close()
        }
        return null
    }

    private fun uploadingPhoto() {
        for(i in 0 until pathList.size){
            val file = getRealPathFromURI(pathList[i])?.let { File(it) }
            if(file != null) fileList.add(file)
        }

        for(i in 0 until fileList.size)
        {
            dbUploadImage(i)
        }
    }

    private fun dbUploadImage(cnt : Int)
    {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), fileList[cnt])
        val body = MultipartBody.Part.createFormData("image", fileList[cnt].name, requestFile)

        val retrofit = Client.getInstance()
        val service = retrofit.create(Service::class.java)

        val hashMap = HashMap<String, RequestBody>()
        hashMap["feed_id"] = userId.toString().toRequestBody()
        service.uploadImage(hashMap, body).enqueue(object : Callback<ImageUpload> {
            override fun onResponse(call: Call<ImageUpload>, response: Response<ImageUpload>) {
            }
            override fun onFailure(call: Call<ImageUpload>, t: Throwable) {
            }
        }
        )
    }

    @SuppressLint("Range")
    fun Uri.asMultipart(contentResolver: ContentResolver): MultipartBody.Part? {
        return contentResolver.query(this, null, null, null, null)?.let {
            if (it.moveToNext()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME));
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
        val intent = Intent()
        setResult(3131,intent)
        finish()
    }

    // 게시 완료
    private fun posting() {
        // 사진이 없는 경우 -> 글만 업로드
        if (photoList.size == 0) {
            postingText(false)
        } else {
            postingText(true)
            //uploadingPhoto()
        }
        cancelPosting()
    }

    // 피드 업로드 (텍스트 만)
    private fun postingText(upload: Boolean) {
        val retrofit = Client.getInstance()
        val loginService = retrofit.create(Service::class.java)
        val mediaType = "application/json".toMediaTypeOrNull()
        val postingText = binding.postingEt.text
        val filterString = getFilterString()
        val param =
            RequestBody.create(mediaType, "{\"content\":\"${postingText}\",\"hash_tags\":[${filterString}]}")
        loginService.getPostingText(param = param)
            .enqueue(object : Callback<response> {
                override fun onResponse(call: Call<response>, response: Response<response>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@WriteFeedActivity, "업로드 완료", Toast.LENGTH_SHORT).show()
                        userId = response.body()?.res?.feedid?.toInt() ?: -1
                        if(upload) uploadingPhoto()
                    }
                }

                override fun onFailure(call: Call<response>, t: Throwable) {
                }
            })
    }

    private fun getFilterString() : String{
        val filterDataList = mutableListOf<Int>()
        // 필터 적용
        lineFilterList.forEachIndexed { index,  it ->
            if(it.backgroundTintList != null && it.backgroundTintList!!.equals(ColorStateList.valueOf(this.resources.getColor(R.color.gray)))){
                filterDataList.add(index + 1)
            }
        }
        val filterString = filterDataList.joinToString(",")
        return filterString
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePhoto(status: Status) {
        photoList.removeAt(status.position)
        binding.rcv.adapter?.notifyDataSetChanged()
    }

    private fun openGallery() {
        status = 2
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.action = Intent.ACTION_GET_CONTENT
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

    private fun takeCamera(){
        status = 1
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                //찍은 사진을 File형식으로 변환
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                if(photoFile != null) fileList.add(photoFile)

                photoFile?.also {
                    camUri = FileProvider.getUriForFile(
                        this,
                        "com.capstone.traffic",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, camUri)
                    imageResult.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //이미지 경로 지정
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            //절대경로 변수에 저장
            fileAbsolutePath = absolutePath
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // 이미지를 받으면 ImageView에 적용한다
            if (result.data?.data == null) {
                photoList.add(Image(camUri))
            }
            else {
                if(status == 2) pathList.add(result.data?.data!!)
                photoList.add(Image(result.data?.data))
            }
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
                            takeCamera()
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
            MEDIA -> {
                TedPermission.create()
                    .setPermissionListener(object : PermissionListener {

                        //권한이 허용됐을 때
                        override fun onPermissionGranted() {
                        }

                        //권한이 거부됐을 때
                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        }
                    })
                    .setDeniedMessage("권한을 허용해주세요.")
                    .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }
        }
    }

    companion object {
        const val MEDIA = 2
        const val CAMERA_PERMISSION = 0
        const val GALLERY_PERMISSION = 1
        private var fileList = arrayListOf<File>()
        lateinit var camUri : Uri
        var status = 0
        private var photoList = arrayListOf<Image>()
        private var pathList = arrayListOf<Uri>()
    }
}
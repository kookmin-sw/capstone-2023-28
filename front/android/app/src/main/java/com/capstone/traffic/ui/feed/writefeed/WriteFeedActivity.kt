package com.capstone.traffic.ui.feed.writefeed

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityWriteFeedBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.ui.feed.writefeed.addImage.Image
import com.capstone.traffic.ui.feed.writefeed.addImage.ListAdapterGrid
import com.capstone.traffic.ui.feed.writefeed.addImage.Status

class WriteFeedActivity : BaseActivity<ActivityWriteFeedBinding>() {
    override var layoutResourceId: Int = R.layout.activity_write_feed
    private lateinit var writeFeedViewModel: WriteFeedViewModel
    private var photoList = arrayListOf<Image>()
    override fun initBinding() {
        writeFeedViewModel = ViewModelProvider(this)[WriteFeedViewModel::class.java]
        binding.write = writeFeedViewModel

        photoList = arrayListOf<Image>(Image(null), Image(null),Image(null), Image(null),Image(null), Image(null))

        binding.run {
            backBtn.setOnClickListener {
                finish()
            }
            // 사진 리사이클러뷰
            var listAdapter = ListAdapterGrid(photoList,
                    onClickDeleteIcon = {
                        deletePhoto(it)
                    }
                )
            rcv.adapter = listAdapter
            rcv.layoutManager = GridLayoutManager(this@WriteFeedActivity,3)

            // 사진 삭제
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun deletePhoto(status : Status){
        photoList.removeAt(status.position)
        binding.rcv.adapter?.notifyDataSetChanged()
    }
}
package com.capstone.traffic.ui.feed.writefeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityCheckOutBinding
import com.capstone.traffic.databinding.ActivityWriteFeedBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.ui.checkout.CheckOutViewModel

class WriteFeedActivity : BaseActivity<ActivityWriteFeedBinding>() {
    override var layoutResourceId: Int = R.layout.activity_write_feed
    private lateinit var writeFeedViewModel: WriteFeedViewModel
    override fun initBinding() {
        writeFeedViewModel = ViewModelProvider(this)[WriteFeedViewModel::class.java]
        binding.write = writeFeedViewModel

        binding.run {
            backBtn.setOnClickListener {
                finish()
            }
        }


    }
}
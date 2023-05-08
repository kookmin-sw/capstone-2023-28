package com.capstone.traffic.ui.feed.find

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.capstone.traffic.databinding.ActivityFindBinding


class FindActivity : AppCompatActivity() {
    private val viewModel: FindViewModel by viewModels()
    private val binding by lazy { ActivityFindBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            // 유저 검색 결과 리사이클러 뷰

        }

    }

}
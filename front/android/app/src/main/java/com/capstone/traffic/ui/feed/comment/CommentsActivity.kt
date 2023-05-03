package com.capstone.traffic.ui.feed.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityCommentsBinding
import com.capstone.traffic.databinding.ActivityHomeBinding
import com.capstone.traffic.ui.HomeViewModel

class CommentsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCommentsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)



    }
}
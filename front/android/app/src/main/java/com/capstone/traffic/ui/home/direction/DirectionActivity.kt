package com.capstone.traffic.ui.home.direction

import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityDirectionBinding
import com.capstone.traffic.global.BaseActivity

class DirectionActivity : BaseActivity<ActivityDirectionBinding>() {
    override var layoutResourceId: Int = R.layout.activity_direction
    private lateinit var directionViewModel: DirectionViewModel
    override fun initBinding() {
        directionViewModel = ViewModelProvider(this)[DirectionViewModel::class.java]
        binding.direction = directionViewModel
    }
}
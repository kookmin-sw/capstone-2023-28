package com.capstone.traffic.ui.checkout

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityCheckOutBinding
import com.capstone.traffic.global.BaseActivity

class CheckOutActivity : BaseActivity<ActivityCheckOutBinding>() {
    override var layoutResourceId: Int = R.layout.activity_check_out
    private lateinit var checkOutViewModel: CheckOutViewModel

    override fun initBinding() {
        checkOutViewModel = ViewModelProvider(this)[CheckOutViewModel::class.java]
        binding.checkout = checkOutViewModel

        checkOutViewModel.status.observe(this, Observer {
            if (it == true) {
                finish()
            }
        })
    }

}
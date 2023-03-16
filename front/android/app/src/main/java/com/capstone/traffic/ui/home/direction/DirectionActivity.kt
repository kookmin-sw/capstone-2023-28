package com.capstone.traffic.ui.home.direction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationSet
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityDirectionBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.SlideUpDialog

class DirectionActivity : BaseActivity<ActivityDirectionBinding>() {
    override var layoutResourceId: Int = R.layout.activity_direction
    private lateinit var directionViewModel: DirectionViewModel
    override fun initBinding() {
        directionViewModel = ViewModelProvider(this)[DirectionViewModel::class.java]
        binding.direction = directionViewModel

        var contentView: View = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.dialog_slide_up, null)
        val slideupPopup = SlideUpDialog.Builder(this)
            .setContentView(contentView)
            .create()

        binding.startEt.setOnClickListener{
            slideupPopup.show()
            contentView.findViewById<Button>(R.id.close).setOnClickListener {
                slideupPopup.dismissAnim()
            }
        }
    }
}
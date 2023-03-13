package com.capstone.traffic.ui.inform.search

import android.content.res.ColorStateList

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivitySearchBinding
import com.capstone.traffic.global.BaseActivity

class SearchActivity() : BaseActivity<ActivitySearchBinding>() {
    override var layoutResourceId: Int = R.layout.activity_search
    private lateinit var searchViewModel : SearchViewModel

    override fun initBinding() {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.search = searchViewModel

        val mainColor = getStationColor(intent.getIntExtra("line",1).toString())
        binding.searchCv.backgroundTintList = ColorStateList.valueOf(mainColor)
        binding.searchIv.backgroundTintList = ColorStateList.valueOf(mainColor)
    }
    private fun getStationColor(line : String) : Int {
        val color = mapOf("1" to R.color.hs1,
            "2" to R.color.hs2,
            "3" to R.color.hs3,
            "4" to R.color.hs4,
            "5" to R.color.hs5,
            "6" to R.color.hs6,
            "7" to R.color.hs7,
            "8" to R.color.hs8,
            "9" to R.color.hs9)
        return if(color[line] != null) this.resources.getColor(color[line]!!) else this.resources.getColor(R.color.black)
    }
}
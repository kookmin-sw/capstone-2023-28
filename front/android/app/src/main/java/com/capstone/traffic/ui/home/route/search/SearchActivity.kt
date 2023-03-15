package com.capstone.traffic.ui.home.route.search

import android.content.Intent
import android.content.res.ColorStateList
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.TextView

import androidx.lifecycle.ViewModelProvider
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivitySearchBinding
import com.capstone.traffic.global.BaseActivity
import com.capstone.traffic.global.MyApplication
import com.capstone.traffic.ui.home.route.search.arrivalInform.ArrivalInformActivity

class SearchActivity() : BaseActivity<ActivitySearchBinding>() {
    override var layoutResourceId: Int = R.layout.activity_search
    private lateinit var searchViewModel : SearchViewModel

    override fun initBinding() {
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.search = searchViewModel

        val mainColor = getStationColor(intent.getIntExtra("line", 1).toString())
        binding.searchCv.backgroundTintList = ColorStateList.valueOf(mainColor)
        binding.searchIv.backgroundTintList = ColorStateList.valueOf(mainColor)

        val searchView = binding.lineSv

        val listAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            MyApplication.allLineData
        )
        binding.lineLv.adapter = listAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (MyApplication.allLineData.contains(query)) {
                    listAdapter.filter.filter(query)
                } else {
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })

        binding.lineLv.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                // 클릭시 엑티비티 이동
                val intent = Intent(this, ArrivalInformActivity::class.java)
                intent.putExtra("station", parent.getItemAtPosition(position).toString())
                startActivity(intent)
            }
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
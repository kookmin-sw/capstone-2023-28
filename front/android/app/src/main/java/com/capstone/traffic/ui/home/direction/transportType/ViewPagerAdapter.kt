package com.capstone.traffic.ui.home.direction.transportType

import BusFragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm : FragmentManager, lifecycle : Lifecycle, val data : java.io.Serializable?): FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position)
        {
            0 -> {
                val fragment = BusFragment()
                val bundle = Bundle()
                bundle.putSerializable("data", data)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = SubwayFragment()
                val bundle = Bundle()
                bundle.putSerializable("data", data)
                fragment.arguments = bundle
                fragment
            }
            2 -> {
                val fragment =  BusAndSubwayFragment()
                val bundle = Bundle()
                bundle.putSerializable("data", data)
                fragment.arguments = bundle
                fragment
            }
            else -> BusFragment()
        }
    }
}
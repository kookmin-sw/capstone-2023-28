package com.capstone.traffic.ui.home.direction.transportType

import BusFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm : FragmentManager, lifecycle : Lifecycle): FragmentStateAdapter(fm,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position)
        {
            0 -> return BusFragment()
            1 -> return SubwayFragment()
            2 -> return BusAndSubwayFragment()
        }
        return BusFragment()
    }
}
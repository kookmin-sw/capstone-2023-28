package com.capstone.traffic.ui.home.direction.transportType

import BusFragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.traffic.model.network.sk.direction.dataClass.itineraries
import com.capstone.traffic.model.network.sk.direction.dataClass.objects

class ViewPagerAdapter(fm : FragmentManager, lifecycle : Lifecycle, val data : objects?): FragmentStateAdapter(fm,lifecycle) {
    lateinit var ser :  List<List<itineraries>>
    init {
        var trans = transportData()
        if(trans != null) ser = trans
    }
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position)
        {
            0 -> {
                val fragment = BusFragment()
                val bundle = Bundle()
                bundle.putSerializable("data", setSerial(ser[1]))
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = SubwayFragment()
                val bundle = Bundle()
                bundle.putSerializable("data", setSerial(ser[0]))
                fragment.arguments = bundle
                fragment
            }
            2 -> {
                val fragment =  BusAndSubwayFragment()
                val bundle = Bundle()
                bundle.putSerializable("data", setSerial(ser[2]))
                fragment.arguments = bundle
                fragment
            }
            else -> BusFragment()
        }
    }
    private fun transportData() : List<List<itineraries>>?
    {
        val data = data
        val onlyBus : MutableList<itineraries> = mutableListOf()
        val onlySubway : MutableList<itineraries> = mutableListOf()
        val subAndBus : MutableList<itineraries> = mutableListOf()

        if(data == null) return null
        data.metaData.plan.itineraries.forEach {
            val usingTransfort = mutableSetOf<String>()
            it.legs.forEach{ legs ->
                usingTransfort.add(legs.mode)
            }
            if( usingTransfort.contains("subway") && usingTransfort.contains("bus")){
                subAndBus.add(it)
            }
            else if(usingTransfort.contains("subway")){
                onlySubway.add(it)
            }
            else if(usingTransfort.contains("bus")){
                onlyBus.add(it)
            }
        }
        return listOf<List<itineraries>>(onlySubway, onlyBus, subAndBus)

    }
    private data class setSerial(val ser: List<itineraries>?) : java.io.Serializable
}
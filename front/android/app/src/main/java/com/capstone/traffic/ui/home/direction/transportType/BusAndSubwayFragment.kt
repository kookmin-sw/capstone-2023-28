package com.capstone.traffic.ui.home.direction.transportType

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.traffic.databinding.FragmentBusAndSubwayBinding

class BusAndSubwayFragment : Fragment() {
    private val binding by lazy { FragmentBusAndSubwayBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bundle = arguments
        val data = bundle?.getSerializable("data")
        print(data)
        binding.text.text = data.toString()
        // Inflate the layout for this fragment
        return binding.root
    }

}
package com.capstone.traffic.ui.home.direction.transportType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentSubwayBinding


class SubwayFragment : Fragment() {
    private val binding by lazy { FragmentSubwayBinding.inflate(layoutInflater) }
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
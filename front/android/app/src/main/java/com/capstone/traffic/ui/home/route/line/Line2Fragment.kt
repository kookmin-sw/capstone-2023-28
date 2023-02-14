package com.capstone.traffic.ui.home.route.line

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.traffic.databinding.FragmentLine2Binding

class Line2Fragment : Fragment() {

    private val binding by lazy { FragmentLine2Binding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return binding.root
    }
}
package com.capstone.traffic.ui.route.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.traffic.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private val binding by lazy { FragmentBoardBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val title = requireArguments().getString("title")
        binding.textView.text = title

        return binding.root
    }

    companion object {
        fun newInstance(title: String) = BoardFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}
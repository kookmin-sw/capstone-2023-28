package com.capstone.traffic.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.traffic.R
import com.capstone.traffic.databinding.FragmentFeedBinding


class FeedFragment : Fragment() {

    private val binding by lazy { FragmentFeedBinding.inflate(layoutInflater) }
    private val FeedViewModel: FeedViewModel by viewModels()
    private lateinit var animHide : Animation
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initAnimaion()

        binding.filterBtn.setOnClickListener {
            binding.filterLl.apply {
                if(visibility == View.GONE) {
                    visibility = View.VISIBLE
                    startAnimation(animHide)
                }
                else{
                    visibility = View.GONE
                }
            }
        }

        return binding.root
    }

    private fun initAnimaion() {
        animHide = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_filter_show)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FeedViewModel.hs.observe(viewLifecycleOwner){

        }
    }
    companion object {
        fun newInstance(title: String) = FeedFragment().apply {
        }
    }
}

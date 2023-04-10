package com.capstone.traffic.ui.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.traffic.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    private val binding by lazy { FragmentMyBinding.inflate(layoutInflater) }
    private val MyViewModel: MyViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyViewModel.follower.observe(viewLifecycleOwner){
            binding.followerTv.text = it.toString()
        }
        MyViewModel.post.observe(viewLifecycleOwner){
            binding.postTv.text = it.toString()
        }
        MyViewModel.following.observe(viewLifecycleOwner){
            binding.followingTv.text = it.toString()
        }
        MyViewModel.nickname.observe(viewLifecycleOwner){
            binding.nicknameTv1.text = it
        }
    }
    companion object {
        fun newInstance(title: String) = MyFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

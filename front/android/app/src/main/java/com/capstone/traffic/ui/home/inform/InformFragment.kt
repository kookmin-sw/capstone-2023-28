package com.capstone.traffic.ui.home.inform

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.traffic.databinding.FragmentInformBinding


class InformFragment : Fragment() {

    private val binding by lazy { FragmentInformBinding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // 화면 전환 on off
        //waitTime()

        return binding.root
    }
    private val rankChangeHandler : Handler by lazy { Handler() }
    private fun waitTime() {
        rankChangeHandler.postDelayed(::changeItem, 3000)
    }
    private fun changeItem(){
        binding.iv1.visibility = if(binding.iv1.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        waitTime()
    }
    companion object {
        fun newInstance(title: String) = InformFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}
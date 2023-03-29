package com.capstone.traffic.ui.home.main

import android.os.Bundle
import androidx.fragment.app.Fragment


class MainFragment : Fragment() {
    companion object {
        fun newInstance(title: String) = MainFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

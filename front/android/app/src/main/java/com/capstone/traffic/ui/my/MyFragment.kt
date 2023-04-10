package com.capstone.traffic.ui.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.traffic.ui.feed.FeedFragment

class MyFragment : Fragment() {
    companion object {
        fun newInstance(title: String) = FeedFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

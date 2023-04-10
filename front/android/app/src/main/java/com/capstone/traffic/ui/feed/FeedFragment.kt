package com.capstone.traffic.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment


class FeedFragment : Fragment() {
    companion object {
        fun newInstance(title: String) = FeedFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
            }
        }
    }
}

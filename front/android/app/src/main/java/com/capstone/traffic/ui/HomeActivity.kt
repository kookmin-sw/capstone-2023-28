package com.capstone.traffic.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityHomeBinding
import com.capstone.traffic.ui.route.board.BoardFragment
import com.capstone.traffic.ui.inform.InformFragment
import com.capstone.traffic.ui.feed.FeedFragment
import com.capstone.traffic.ui.my.MyFragment
import com.capstone.traffic.ui.route.route.RouteFragment

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vm = viewModel

        viewModel.currentPageType.observe(this) {
            changeFragment(it)
        }
    }

    private fun changeFragment(pageType: PageType) {
        val transaction = supportFragmentManager.beginTransaction()
        var targetFragment = supportFragmentManager.findFragmentByTag(pageType.tag)
        if (targetFragment == null) {
            targetFragment = getFragment(pageType)
            transaction.add(R.id.container, targetFragment, pageType.tag)
        }
        transaction.show(targetFragment)
        PageType.values()
            .filterNot { it == pageType }
            .forEach { type ->
                supportFragmentManager.findFragmentByTag(type.tag)?.let {
                    transaction.hide(it)
                }
            }
        transaction.commitAllowingStateLoss()
    }

    private fun getFragment(pageType: PageType): Fragment {
        var fragment : Fragment = FeedFragment.newInstance(pageType.title)
        when (pageType.title)
        {
            "feed" -> fragment = FeedFragment.newInstance(pageType.title)
            "magazine" -> fragment =  BoardFragment.newInstance(pageType.title)
            "route" -> fragment = RouteFragment.newInstance(pageType.title)
            "inform" -> fragment = InformFragment.newInstance(pageType.title)
            "my" -> fragment = MyFragment.newInstance(pageType.title)
        }
        return fragment
    }
}

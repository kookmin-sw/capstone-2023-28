package com.capstone.traffic.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import com.capstone.traffic.R
import com.capstone.traffic.databinding.ActivityHomeBinding
import com.capstone.traffic.ui.home.board.BoardFragment
import com.capstone.traffic.ui.home.inform.InformFragment
import com.capstone.traffic.ui.home.main.MainFragment
import com.capstone.traffic.ui.home.route.RouteFragment

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
        var fragment : Fragment = MainFragment.newInstance(pageType.title)
        when (pageType.title)
        {
            "main" -> fragment = MainFragment.newInstance(pageType.title)
            "board" -> fragment =  BoardFragment.newInstance(pageType.title)
            "route" -> fragment = RouteFragment.newInstance(pageType.title)
            "inform" -> fragment = InformFragment.newInstance(pageType.title)
        }
        return fragment
    }
}

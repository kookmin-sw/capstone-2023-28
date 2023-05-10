package com.capstone.traffic.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.capstone.traffic.R
import com.capstone.traffic.ui.feed.hs

class HomeViewModel : ViewModel() {


    private val _currentPageType = MutableLiveData(PageType.PAGE1)
    val currentPageType: LiveData<PageType> = _currentPageType

    private val _selectedLine = MutableLiveData<Int>()
    val selectedLine : LiveData<Int> = _selectedLine

    private val _hs1 = MutableLiveData<Boolean>()
    val hs1 : LiveData<Boolean> get() = _hs1

    init {
        _selectedLine.value = 1
        _hs1.value = false
    }

    fun clicking() {
        _hs1.value = _hs1.value ?: false
    }

    fun line1() {_selectedLine.value = 1}
    fun line2() {_selectedLine.value = 2}
    fun line3() {_selectedLine.value = 3}
    fun line4() {_selectedLine.value = 4}
    fun line5() {_selectedLine.value = 5}
    fun line6() {_selectedLine.value = 6}
    fun line7() {_selectedLine.value = 7}
    fun line8() {_selectedLine.value = 8}
    fun line9() {_selectedLine.value = 9}


    fun setCurrentPage(menuItemId: Int): Boolean {
        val pageType = getPageType(menuItemId)
        changeCurrentPage(pageType)

        return true
    }

    private fun getPageType(menuItemId: Int): PageType {
        return when (menuItemId) {
            R.id.feed -> PageType.PAGE1
            //R.id.magazine -> PageType.PAGE2
            R.id.route -> PageType.PAGE2
            R.id.inform -> PageType.PAGE3
            R.id.my -> PageType.PAGE4
            else -> throw IllegalArgumentException("not found menu item id")
        }
    }

    private fun changeCurrentPage(pageType: PageType) {
        if (currentPageType.value == pageType) return
        _currentPageType.value = pageType
    }
}
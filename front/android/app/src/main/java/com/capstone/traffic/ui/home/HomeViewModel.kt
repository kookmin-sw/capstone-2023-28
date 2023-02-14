package com.capstone.traffic.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.traffic.R
class HomeViewModel : ViewModel() {

    private val _currentPageType = MutableLiveData(PageType.PAGE1)
    val currentPageType: LiveData<PageType> = _currentPageType

    private val _selectedLine = MutableLiveData<Int>()
    private val _beforeSelectedLine = MutableLiveData<Int>()
    val beforeSelectedLine : LiveData<Int> = _beforeSelectedLine
    val selectedLine : LiveData<Int> = _selectedLine

    init {
        _selectedLine.value = 2
    }

    fun line1() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 1}
    fun line2() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 2}
    fun line3() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 3}
    fun line4() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 4}
    fun line5() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 5}
    fun line6() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 6}
    fun line7() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 7}
    fun line8() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 8}
    fun line9() {_beforeSelectedLine.value = _selectedLine.value; _selectedLine.value = 9}


    fun setCurrentPage(menuItemId: Int): Boolean {
        val pageType = getPageType(menuItemId)
        changeCurrentPage(pageType)

        return true
    }

    private fun getPageType(menuItemId: Int): PageType {
        return when (menuItemId) {
            R.id.board -> PageType.PAGE1
            R.id.home -> PageType.PAGE2
            R.id.route -> PageType.PAGE3
            else -> throw IllegalArgumentException("not found menu item id")
        }
    }

    private fun changeCurrentPage(pageType: PageType) {
        if (currentPageType.value == pageType) return

        _currentPageType.value = pageType
    }
}
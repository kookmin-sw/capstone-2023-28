package com.capstone.traffic.ui.feed

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide.init
import com.capstone.traffic.global.viewmodel.BaseViewModel

class FeedViewModel() : BaseViewModel(){

    private val _itemSelectedNum = MutableLiveData<View>()
    val itemSelectedNum get() = _itemSelectedNum
    companion object {
        const val EVENT_START_FILTER_APPLY = 22212
        const val EVENT_START_FILTER_SELECT = 22213
    }

    fun filterApply() = viewEvent(EVENT_START_FILTER_APPLY)
    fun filterSelect(view : View) {
        itemSelectedNum.value = view
        viewEvent(EVENT_START_FILTER_SELECT)
    }
}
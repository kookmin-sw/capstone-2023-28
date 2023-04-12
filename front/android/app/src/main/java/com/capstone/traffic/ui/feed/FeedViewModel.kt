package com.capstone.traffic.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide.init

class FeedViewModel(application: Application) : AndroidViewModel(application){
    private val _hs = MutableLiveData<hs>()

    val hs : LiveData<hs> = _hs
    init {
        _hs.value = hs()
    }

    fun selectedLineFilter(s : Int){
        when(s)
        {
            1 -> _hs.value?.hs1 = if(_hs.value?.hs1!!) false else true
        }
    }
}
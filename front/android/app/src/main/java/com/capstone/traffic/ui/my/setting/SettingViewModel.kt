package com.capstone.traffic.ui.my.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.traffic.global.MyApplication

class SettingViewModel: ViewModel() {
    private val _alert = MutableLiveData<Boolean>()
    val alert : LiveData<Boolean> = _alert

    init {
        _alert.value = MyApplication.prefs.getAlert()
    }


}
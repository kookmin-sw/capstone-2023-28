package com.capstone.traffic.ui.home.direction

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DirectionViewModel : ViewModel(){
    var status = MutableLiveData<Boolean>(true)
}
package com.capstone.traffic.ui.route.direction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DirectionViewModel : ViewModel(){
    var status = MutableLiveData<Boolean>(true)
}
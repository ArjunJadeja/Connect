package com.arjun.connect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NetworkViewModel : ViewModel() {

    private val _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> = _isNetworkConnected

    fun getNetwork(isNetworkConnected: Boolean) {
        _isNetworkConnected.value = isNetworkConnected
    }
}
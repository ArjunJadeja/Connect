package com.arjun.connect.viewmodel

import androidx.lifecycle.ViewModel
import com.arjun.connect.data.StatusDao
import com.arjun.connect.repository.StatusRepository

class StatusViewModel : ViewModel() {

    private val repository: StatusRepository

    init {
        val dao = StatusDao
        repository = StatusRepository(dao)
    }

    suspend fun getStatus(): String? {
        return repository.getStatus()
    }

    fun updateStatus(status: String) {
        repository.updateStatus(status)
    }
}
package com.arjun.connect.repository

import com.arjun.connect.data.StatusDao

class StatusRepository(private val statusDao: StatusDao) {

    suspend fun getStatus(): String? {
        return statusDao.getStatus()
    }

    fun updateStatus(status: String) {
        statusDao.updateStatus(status)
    }
}
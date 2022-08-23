package com.arjun.connect.viewmodel

import androidx.lifecycle.ViewModel
import com.arjun.connect.data.RegistrationDao
import com.arjun.connect.repository.RegistrationRepository

class RegistrationViewModel : ViewModel() {

    private val repository: RegistrationRepository

    init {
        val dao = RegistrationDao
        repository = RegistrationRepository(dao)
    }

    val currentUser = repository.currentUser

    suspend fun userRegistered(userUid: String): Boolean {
        return repository.userRegistered(userUid)
    }

    fun registerUser(userUid: String, photoUrl: String, displayName: String) {
        repository.registerUser(userUid, photoUrl, displayName)
    }

    suspend fun profileSet(): Boolean {
        return repository.profileSet()
    }
}
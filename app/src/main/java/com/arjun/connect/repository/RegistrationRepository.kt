package com.arjun.connect.repository

import com.arjun.connect.data.RegistrationDao
import com.google.firebase.auth.FirebaseAuth

class RegistrationRepository(private val registrationDao: RegistrationDao) {

    val currentUser = FirebaseAuth.getInstance().currentUser

    suspend fun userRegistered(userUid: String): Boolean {
        return registrationDao.userRegistered(userUid)
    }

    fun registerUser(userUid: String, photoUrl: String, displayName: String) {
        registrationDao.registerUser(userUid, photoUrl, displayName)
    }

    suspend fun profileSet(): Boolean {
        return registrationDao.profileSet()
    }
}
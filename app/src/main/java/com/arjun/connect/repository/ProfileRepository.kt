package com.arjun.connect.repository

import android.net.Uri
import com.arjun.connect.data.ProfileDao
import com.google.firebase.firestore.DocumentSnapshot

class ProfileRepository(private val profileDao: ProfileDao) {

    suspend fun getUserInfo(): DocumentSnapshot {
        return profileDao.getUserInfo()
    }

    fun uploadImage(imageUri: Uri) {
        profileDao.uploadImage(imageUri)
    }

    suspend fun usernameExists(username: String): Boolean {
        return profileDao.usernameExists(username)
    }

    fun updateProfileInfo(name: String, username: String, status: String) {
        profileDao.updateProfileInfo(name, username, status)
    }

    fun updateSocialInfo(
        linkedInProfile: String?,
        twitterProfile: String?,
        instagramProfile: String?,
        whatsappProfile: String?
    ) {
        profileDao.updateSocialInfo(
            linkedInProfile,
            twitterProfile,
            instagramProfile,
            whatsappProfile
        )
    }
}
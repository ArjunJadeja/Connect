package com.arjun.connect.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.arjun.connect.data.ProfileDao
import com.arjun.connect.repository.ProfileRepository
import com.google.firebase.firestore.DocumentSnapshot

class ProfileViewModel : ViewModel() {

    private val repository: ProfileRepository

    init {
        val dao = ProfileDao
        repository = ProfileRepository(dao)
    }

    suspend fun getUserInfo(): DocumentSnapshot {
        return repository.getUserInfo()
    }

    fun uploadImage(imageUri: Uri) {
        repository.uploadImage(imageUri)
    }

    suspend fun usernameExists(username: String): Boolean {
        return repository.usernameExists(username)
    }

    fun updateProfileInfo(name: String, username: String, status: String) {
        repository.updateProfileInfo(name, username, status)
    }

    fun updateSocialInfo(
        linkedInProfile: String?,
        twitterProfile: String?,
        instagramProfile: String?,
        whatsappProfile: String?
    ) {
        repository.updateSocialInfo(
            linkedInProfile,
            twitterProfile,
            instagramProfile,
            whatsappProfile
        )
    }
}
package com.arjun.connect.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object ProfileDao {

    private val userRef = FirebaseFirestore.getInstance().collection("users")

    suspend fun getUserInfo(): DocumentSnapshot {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return userRef.document(currentUser!!.uid).get().await()
    }

    fun uploadImage(imageUri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val storageRef = FirebaseStorage.getInstance().getReference("profileImages")
        CoroutineScope(Dispatchers.IO).launch {
            storageRef.child(currentUser!!.uid).putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.child(currentUser.uid).downloadUrl.addOnSuccessListener { cloudStorageUri ->
                        updateImageUrl(cloudStorageUri.toString())
                    }
                }.await()
        }
    }

    private fun updateImageUrl(imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.IO).launch {
            userRef.document(currentUser!!.uid).update(mapOf("profileImageUrl" to imageUrl)).await()
        }
    }

    suspend fun usernameExists(username: String): Boolean {
        val documentSnapshot = userRef.whereEqualTo("username", username).get().await()
        return !documentSnapshot.isEmpty
    }

    fun updateProfileInfo(
        name: String,
        username: String,
        status: String
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.IO).launch {
            userRef.document(currentUser!!.uid).update(
                mapOf(
                    "name" to name,
                    "username" to username,
                    "status" to status
                )
            ).await()
        }
    }

    fun updateSocialInfo(
        linkedInProfile: String?,
        twitterProfile: String?,
        instagramProfile: String?,
        whatsappProfile: String?
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.IO).launch {
            userRef.document(currentUser!!.uid)
                .update(
                    mapOf(
                        "linkedInUserName" to linkedInProfile,
                        "twitterUserName" to twitterProfile,
                        "instagramUserName" to instagramProfile,
                        "whatsAppNumber" to whatsappProfile
                    )
                ).await()
        }
    }
}
package com.arjun.connect.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object RegistrationDao {

    private val userRef = FirebaseFirestore.getInstance().collection("users")

    suspend fun userRegistered(userUid: String): Boolean {
        val documentSnapshot = userRef.document(userUid).get().await()
        return documentSnapshot.exists()
    }

    fun registerUser(userUid: String, photoUrl: String, displayName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = User(
                userUid,
                photoUrl,
                displayName,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                arrayListOf(),
                null
            )
            userRef.document(userUid).set(user).await()
        }
    }

    suspend fun profileSet(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val documentSnapshot =
            userRef.document(currentUser!!.uid).get().await().toObject(User::class.java)
        return documentSnapshot!!.username?.isNotEmpty() ?: false
    }
}
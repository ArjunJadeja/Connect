package com.arjun.connect.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object StatusDao {

    private val userRef = FirebaseFirestore.getInstance().collection("users")

    suspend fun getStatus(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val documentSnapshot =
            userRef.document(currentUser!!.uid).get().await().toObject(User::class.java)
        return documentSnapshot?.status
    }

    fun updateStatus(status: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.IO).launch {
            userRef.document(currentUser!!.uid).update(mapOf("status" to status)).await()
        }
    }
}
package com.arjun.connect.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object ConnectionDao {

    private val userRef = FirebaseFirestore.getInstance().collection("users")

    fun addUser(userId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.IO).launch {
            val user =
                userRef.document(currentUser!!.uid).get().await().toObject(User::class.java)!!
            val userAdded = user.addedUsers.contains(userId)
            if (!userAdded) {
                user.addedUsers.add(userId)
            }
            userRef.document(currentUser.uid).set(user).await()
        }
    }

    fun removeUser(userId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        CoroutineScope(Dispatchers.IO).launch {
            val user =
                userRef.document(currentUser!!.uid).get().await().toObject(User::class.java)!!
            val addedUser = user.addedUsers.contains(userId)
            if (addedUser) {
                user.addedUsers.remove(userId)
            }
            userRef.document(currentUser.uid).set(user).await()
        }
    }

    suspend fun getAddedUsers(): MutableList<String> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val documentSnapshot =
            userRef.document(currentUser!!.uid).get().await().toObject(User::class.java)
        return documentSnapshot?.addedUsers ?: mutableListOf(currentUser.uid)
    }

    fun addedUsersQuery(userNamesList: MutableList<String>): Query {
        return userRef.whereIn(FieldPath.documentId(), userNamesList)
    }

    fun searchUserQuery(username: String): Query {
        return userRef.whereGreaterThanOrEqualTo("username", username)
            .orderBy("username", Query.Direction.ASCENDING).startAt(username).endAt("$username~")
            .limit(10)
    }
}
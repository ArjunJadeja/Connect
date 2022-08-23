package com.arjun.connect.viewmodel

import androidx.lifecycle.ViewModel
import com.arjun.connect.data.ConnectionDao
import com.arjun.connect.repository.ConnectionsRepository
import com.google.firebase.firestore.Query

class ConnectionsViewModel : ViewModel() {

    private val repository: ConnectionsRepository

    init {
        val dao = ConnectionDao
        repository = ConnectionsRepository(dao)
    }

    fun addUser(userId: String) {
        repository.addUser(userId)
    }

    fun removeUser(userId: String) {
        repository.removeUser(userId)
    }

    fun addedUsersQuery(userNamesList: MutableList<String>): Query {
        return repository.addedUsersQuery(userNamesList)
    }

    suspend fun getAddedUsers(): MutableList<String> {
        return repository.getAddedUsers()
    }

    fun searchUserQuery(username: String): Query {
        return repository.searchUserQuery(username)
    }
}
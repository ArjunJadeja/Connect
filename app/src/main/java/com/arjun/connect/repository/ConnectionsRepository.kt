package com.arjun.connect.repository

import com.arjun.connect.data.ConnectionDao
import com.google.firebase.firestore.Query

class ConnectionsRepository(private val connectionDao: ConnectionDao) {

    fun addUser(userId: String) {
        connectionDao.addUser(userId)
    }

    fun removeUser(userId: String) {
        connectionDao.removeUser(userId)
    }

    suspend fun getAddedUsers(): MutableList<String> {
        return connectionDao.getAddedUsers()
    }

    fun addedUsersQuery(userNamesList: MutableList<String>): Query {
        return connectionDao.addedUsersQuery(userNamesList)
    }

    fun searchUserQuery(username: String): Query {
        return connectionDao.searchUserQuery(username)
    }
}
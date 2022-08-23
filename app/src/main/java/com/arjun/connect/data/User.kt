package com.arjun.connect.data

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Keep
@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val profileImageUrl: String? = "",
    val name: String? = "",
    val username: String? = "",
    val status: String? = "",
    @ServerTimestamp val statusUpdateTime: Timestamp? = null,
    val linkedInUserName: String? = "",
    val twitterUserName: String? = "",
    val instagramUserName: String? = "",
    val whatsAppNumber: String? = "",
    val addedUsers: ArrayList<String> = ArrayList(),
    @ServerTimestamp val joiningTime: Timestamp? = null
)
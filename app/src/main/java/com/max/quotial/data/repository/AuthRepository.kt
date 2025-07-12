package com.max.quotial.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    fun getUser(): FirebaseUser {
        return auth.currentUser ?: throw Exception("Error while reading user")
    }

    fun getUserId(): String {
        return getUser().uid
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
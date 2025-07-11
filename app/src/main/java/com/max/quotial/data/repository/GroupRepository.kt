package com.max.quotial.data.repository

import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.max.quotial.data.model.Group
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupRepository {
    private val database = Firebase.database

    suspend fun createGroup(name: String, creatorId: String, description: String?): Result<Group> =
        suspendCoroutine { continuation ->
            val id = database.getReference("groups").push().key ?: run {
                continuation.resume(Result.failure(Exception("Error while creating group ID")))
                return@suspendCoroutine
            }

            val group = Group(
                id,
                name,
                description = description ?: "",
                createdBy = creatorId,
                createdAt = System.currentTimeMillis(),
                memberCount = 0,
            )

            database.getReference("groups").child(id).setValue(group)
                .addOnSuccessListener {
                    continuation.resume(Result.success(group))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }
        }

    fun joinGroup(groupId: String, userId: String) {}
    fun leaveGroup(groupId: String, userId: String) {}

    fun getUserGroups() {}
}
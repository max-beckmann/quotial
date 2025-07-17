package com.max.quotial.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.max.quotial.data.model.Group
import com.max.quotial.data.model.GroupMember
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class GroupRepository @Inject constructor() {
    private val database = Firebase.database

    suspend fun createGroup(name: String, creatorId: String, description: String?): Result<Group> =
        suspendCoroutine { continuation ->
            val query = database.getReference("groups").orderByChild("name").equalTo(name)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        continuation.resume(Result.failure(Exception("Error while creating group: name is already taken")))
                        return
                    }

                    val id = database.getReference("groups").push().key ?: run {
                        continuation.resume(Result.failure(Exception("Error while creating group ID")))
                        return
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

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(Result.failure(Exception(error.message)))
                }
            })
        }

    suspend fun joinGroup(groupId: String, userId: String): Result<Unit> =
        suspendCoroutine { continuation ->
            val updates = mutableMapOf<String, Any?>()

            val groupMember = GroupMember(
                userId,
                role = "member",
                joinedAt = System.currentTimeMillis()
            )

            updates["groupMembers/$groupId/$userId"] = groupMember
            updates["userGroups/$userId/$groupId"] = true

            database.getReference("groups").child(groupId).child("memberCount")
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        val currentCount = currentData.getValue(Int::class.java) ?: 0
                        currentData.value = currentCount + 1
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (committed) {
                            database.reference.updateChildren(updates)
                        }
                    }
                })
        }

    suspend fun leaveGroup(groupId: String, userId: String): Result<Unit> =
        suspendCoroutine { continuation ->
            database.getReference("groups").child(groupId).child("memberCount")
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        val currentCount = currentData.getValue(Int::class.java) ?: 0
                        currentData.value = currentCount - 1
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?,
                        committed: Boolean,
                        currentData: DataSnapshot?
                    ) {
                        if (committed) {
                            database.getReference("groupMembers").child(groupId).child(userId)
                                .removeValue()

                            database.getReference("userGroups").child(userId).child(groupId)
                                .removeValue()
                        }
                    }
                })
        }

    fun getAllGroups(): Flow<List<Group>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val groups = mutableListOf<Group>()
                for (childSnapshot in dataSnapshot.children) {
                    val group = childSnapshot.getValue(Group::class.java)
                    group?.let { groups.add(it) }
                }

                trySend(groups)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "GroupRepository",
                    "Error while reading groups",
                    databaseError.toException()
                )

                close(databaseError.toException())
            }
        }

        val groupsRef = database.getReference("groups")

        groupsRef.addValueEventListener(listener)
        awaitClose { groupsRef.removeEventListener(listener) }
    }

    fun getUserGroups(userId: String): Flow<List<String>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val groups = mutableListOf<String>()
                for (childSnapshot in dataSnapshot.children) {
                    val group = childSnapshot.key
                    if (group != null) groups.add(group)
                }

                trySend(groups)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "GroupRepository",
                    "Error while reading user groups",
                    databaseError.toException()
                )

                close(databaseError.toException())
            }
        }

        val userGroupsRef = database.getReference("userGroups").child(userId)

        userGroupsRef.addValueEventListener(listener)
        awaitClose { userGroupsRef.removeEventListener(listener) }
    }
}
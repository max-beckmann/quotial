package com.max.quotial.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.max.quotial.data.model.Post
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebasePostRepository {
    private val database =
        Firebase.database("https://quotial-667ab-default-rtdb.europe-west1.firebasedatabase.app")
    private val postsRef = database.getReference("posts")

    fun getPosts(): Flow<List<Post>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts = mutableListOf<Post>()
                for (childSnapshot in dataSnapshot.children) {
                    val post = childSnapshot.getValue(Post::class.java)
                    post?.let { posts.add(it) }
                }

                trySend(posts.sortedByDescending { it.timestamp })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "[FirebasePostRepository]",
                    "Error while reading posts",
                    databaseError.toException()
                )

                close(databaseError.toException())
            }
        }

        postsRef.addValueEventListener(listener)
        awaitClose { postsRef.removeEventListener(listener) }
    }

    suspend fun createPost(content: String): Result<Post> = suspendCoroutine { continuation ->
        val id = postsRef.push().key ?: run {
            continuation.resume(Result.failure(Exception("Error while creating post ID")))
            return@suspendCoroutine
        }

        val post = Post(
            id,
            content = content,
            timestamp = System.currentTimeMillis(),
            userId = "",
        )

        postsRef.child(id).setValue(post)
            .addOnSuccessListener {
                continuation.resume(Result.success(post))
            }
            .addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }
    }
}
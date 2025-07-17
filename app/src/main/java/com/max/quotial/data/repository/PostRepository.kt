package com.max.quotial.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.max.quotial.data.model.Group
import com.max.quotial.data.model.Post
import com.max.quotial.data.model.Quote
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class PostRepository @Inject constructor() {
    private val database = Firebase.database
    private val postsRef = database.getReference("posts")

    private var postListener: ValueEventListener? = null

    fun getPosts(): Flow<List<Post>> = callbackFlow {
        postListener = object : ValueEventListener {
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
                    "FirebasePostRepository",
                    "Error while reading posts",
                    databaseError.toException()
                )

                close(databaseError.toException())
            }
        }

        postsRef.addValueEventListener(postListener!!)
        awaitClose { postsRef.removeEventListener(postListener!!) }
    }

    suspend fun createPost(quote: Quote, user: FirebaseUser, group: Group?): Result<Post> =
        suspendCoroutine { continuation ->
            val id = postsRef.push().key ?: run {
                continuation.resume(Result.failure(Exception("Error while creating post ID")))
                return@suspendCoroutine
            }

            val post = Post(
                id,
                timestamp = System.currentTimeMillis(),
                userId = user.uid,
                username = user.displayName ?: "Anonymous",
                groupId = group?.id,
                groupName = group?.name ?: "General",
                quote,
            )

            postsRef.child(id).setValue(post)
                .addOnSuccessListener {
                    continuation.resume(Result.success(post))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }
        }

    suspend fun deletePost(id: String): Result<Unit> = suspendCoroutine { continuation ->
        postsRef.child(id).removeValue()
            .addOnSuccessListener {
                continuation.resume(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                continuation.resume(Result.failure(exception))
            }
    }

    fun stopListening() {
        postListener?.let { postsRef.removeEventListener(it) }
        postListener = null
    }
}
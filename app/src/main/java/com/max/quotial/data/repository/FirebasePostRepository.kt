package com.max.quotial.data.repository

import com.max.quotial.data.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FirebasePostRepository {
    /*private val database =
        Firebase.database("https://quotial-667ab-default-rtdb.europe-west1.firebasedatabase.app")
    private val postsRef = database.getReference("posts")*/

    fun getPosts(): Flow<List<Post>> = flow {}

    fun createPost(content: String) {}
}
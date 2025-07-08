package com.max.quotial.data.repository

import com.google.firebase.Firebase
import com.google.firebase.database.database

class VoteRepository {
    private val database = Firebase.database
    private val votesRef = database.getReference("votes")

    fun vote(postId: String, userId: String, vote: Int) {
        votesRef.child(postId).child(userId).setValue(vote)
        /* TODO: add transaction for updating rating on post */
    }
}
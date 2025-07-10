package com.max.quotial.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.max.quotial.data.model.Post
import com.max.quotial.data.model.Vote
import com.max.quotial.data.model.VoteType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class VoteRepository {
    private val database = Firebase.database

    fun getVotesForUser(userId: String): Flow<Map<String, VoteType>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val votes = mutableMapOf<String, VoteType>()

                for (voteSnapshot in snapshot.children) {
                    votes.put(
                        voteSnapshot.key!!,
                        voteSnapshot.getValue(VoteType::class.java) ?: VoteType.NONE
                    )
                }

                trySend(votes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    "VoteRepository",
                    "Error while reading votes",
                    error.toException()
                )

                close(error.toException())
            }
        }

        val userVotesRef = database.getReference("userVotes").child(userId)

        userVotesRef.addValueEventListener(listener)
        awaitClose { userVotesRef.removeEventListener(listener) }
    }

    suspend fun vote(postId: String, userId: String, vote: VoteType): Result<Unit> {
        return try {
            val post = database
                .getReference("posts")
                .child(postId)
                .get()
                .await()
                .getValue(Post::class.java)
                ?: return Result.failure(Exception("post not found"))

            val previousVote = database
                .getReference("userVotes")
                .child(userId)
                .child(postId)
                .get()
                .await()
                .getValue(VoteType::class.java)
                ?: VoteType.NONE

            var updatedUpvotes = post.upvotes
            var updatedDownvotes = post.downvotes

            when (previousVote) {
                VoteType.UPVOTE -> updatedUpvotes--
                VoteType.DOWNVOTE -> updatedDownvotes--
                VoteType.NONE -> {}
            }

            when (vote) {
                VoteType.UPVOTE -> updatedUpvotes++
                VoteType.DOWNVOTE -> updatedDownvotes++
                VoteType.NONE -> {}
            }

            val updates = mutableMapOf<String, Any?>()

            updates["posts/$postId/upvotes"] = updatedUpvotes
            updates["posts/$postId/downvotes"] = updatedDownvotes

            if (vote == VoteType.NONE) {
                updates["votes/$postId/$userId"] = null
                updates["userVotes/$userId/$postId"] = null
            } else {
                updates["votes/$postId/$userId"] = Vote(vote, System.currentTimeMillis())
                updates["userVotes/$userId/$postId"] = vote.name
            }

            database.reference.updateChildren(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
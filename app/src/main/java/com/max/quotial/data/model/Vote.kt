package com.max.quotial.data.model

enum class VoteType {
    UPVOTE, DOWNVOTE, NONE
}

data class Vote(
    val type: VoteType = VoteType.NONE,
    val timestamp: Long = System.currentTimeMillis()
)

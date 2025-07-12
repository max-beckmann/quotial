package com.max.quotial.data.model

data class Post(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
    val username: String = "",
    val groupId: String? = null,
    val groupName: String? = null,
    val quote: Quote = Quote(),
    val upvotes: Int = 0,
    val downvotes: Int = 0,
) {
}

val Post.score: Int get() = upvotes - downvotes
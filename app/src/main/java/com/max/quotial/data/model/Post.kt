package com.max.quotial.data.model

data class Post(
    val id: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
) {
}

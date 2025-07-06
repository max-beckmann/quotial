package com.max.quotial.data.model

data class Post(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "",
    val username: String = "",
    val quote: Quote = Quote(),
) {
}

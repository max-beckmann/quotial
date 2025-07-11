package com.max.quotial.data.model

data class Group(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val memberCount: Int = 0,
    val imageUrl: String? = null,
)

data class GroupMember(
    val userId: String = "",
    val groupId: String = "",
    val joinedAt: Long = System.currentTimeMillis(),
)

package com.example.donex.model

data class Chat(
    val id: String = "",
    val lastMessage: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userIds: List<String> = emptyList(),
    val userNames: Map<String, String> = emptyMap()
)


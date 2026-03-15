package com.example.donex.model


data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val conteudo: String = "",
    val tipo: PostType = PostType.OFFER,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
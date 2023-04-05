package com.vchat.data.models

/**
 * Created by Fasil on 26/03/23.
 */
data class Chat(
    val roomId: String = "",
    val email: String = "",
    val name: String = "",
    val profileUrl: String = "",
    val message: String = "",
    val messageCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

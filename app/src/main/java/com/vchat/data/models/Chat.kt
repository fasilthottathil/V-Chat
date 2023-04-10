package com.vchat.data.models

/**
 * Created by Fasil on 26/03/23.
 */
data class Chat(
    var roomId: String = "",
    var email: String = "",
    var name: String = "",
    var profileUrl: String = "",
    var message: String = "",
    var messageCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMap() = mapOf(
        "roomId" to roomId,
        "email" to email,
        "name" to name,
        "profileUrl" to profileUrl,
        "message" to message,
        "messageCount" to messageCount,
        "timestamp" to timestamp
    )
}

package com.vchat.data.models

import com.vchat.common.enums.MessageType

/**
 * Created by Fasil on 09/04/23.
 */
data class Message(
    var id: String = "",
    var roomId: String = "",
    var userId: String = "",
    var message: String = "",
    var messageType: String = MessageType.TEXT.name,
    val isDeleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

package com.vchat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vchat.common.enums.MessageType

/**
 * Created by Fasil on 09/04/23.
 */
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val roomId: String = "",
    val userId: String = "",
    val message: String = "",
    val messageType: String = MessageType.TEXT.name,
    val isDeleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

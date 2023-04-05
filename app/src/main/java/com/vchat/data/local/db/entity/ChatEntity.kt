package com.vchat.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Fasil on 26/03/23.
 */
@Entity(tableName = "Chats", indices = [Index(value = ["roomId"])])
data class ChatEntity(
    @PrimaryKey(autoGenerate = false)
    val roomId: String = "",
    val email: String = "",
    val name: String = "",
    val profileUrl: String = "",
    val message: String = "",
    val messageCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

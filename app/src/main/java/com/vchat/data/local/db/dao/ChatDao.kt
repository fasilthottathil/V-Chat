package com.vchat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.vchat.data.local.db.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 09/04/23.
 */
@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertMessage(messageEntity: MessageEntity)

    @Upsert
    suspend fun upsertMessages(messageEntityList: List<MessageEntity>)

    @Delete
    fun deleteMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM messages WHERE isDeleted = 0 AND roomId = :roomId ORDER BY timestamp ASC")
    fun getMessages(roomId: String): Flow<List<MessageEntity>>
}
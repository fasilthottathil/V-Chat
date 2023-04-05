package com.vchat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vchat.data.local.db.entity.ChatEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 26/03/23.
 */
@Dao
interface ChatsDao {
    @Upsert
    suspend fun upsertChat(chatEntity: ChatEntity)

    @Upsert
    suspend fun upsertChats(chatEntityList: List<ChatEntity>)

    @Query("SELECT * FROM Chats ORDER BY timestamp DESC")
    fun getChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM Chats WHERE email =:email")
    fun getChatByEmail(email: String): ChatEntity?

}
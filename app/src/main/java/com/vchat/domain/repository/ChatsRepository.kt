package com.vchat.domain.repository

import com.vchat.common.Response
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.data.models.Chat
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 26/03/23.
 */
interface ChatsRepository {
    suspend fun getChatsFromServer()
    suspend fun startChat(chat: Chat, friendId: String): Response<ChatEntity>
    suspend fun getChatsFromLocal(): Flow<List<ChatEntity>>
    suspend fun incrementCountAndUpdateMessage(userId: String, roomId: String, message: String)
    suspend fun clearMessageCount(userId: String, roomId: String)
    suspend fun addMessageToChat(userId: String, roomId: String, message: String)
}
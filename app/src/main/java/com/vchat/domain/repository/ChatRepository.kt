package com.vchat.domain.repository

import android.net.Uri
import com.vchat.common.Response
import com.vchat.data.local.db.entity.MessageEntity
import com.vchat.data.models.Message
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 09/04/23.
 */
interface ChatRepository {
    suspend fun getMessages(roomId: String)
    suspend fun sendMessage(roomId: String, message: Message): Response<MessageEntity>
    suspend fun deleteMessage(roomId: String, messageEntity: MessageEntity): Response<MessageEntity>
    suspend fun getMessagesFromLocal(roomId: String): Flow<List<MessageEntity>>
    suspend fun uploadMessageImage(roomId: String, id: String, imageUri: Uri): Response<String>
}
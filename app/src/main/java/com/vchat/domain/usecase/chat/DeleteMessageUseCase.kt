package com.vchat.domain.usecase.chat

import com.vchat.data.local.db.entity.MessageEntity
import com.vchat.data.repository.ChatRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class DeleteMessageUseCase @Inject constructor(private val chatRepositoryImpl: ChatRepositoryImpl) {
    suspend fun execute(
        roomId: String,
        messageEntity: MessageEntity
    ) = chatRepositoryImpl.deleteMessage(roomId, messageEntity)
}
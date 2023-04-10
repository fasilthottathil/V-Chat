package com.vchat.domain.usecase.chat

import com.vchat.data.models.Message
import com.vchat.data.repository.ChatRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class SendMessageUseCase @Inject constructor(private val chatRepositoryImpl: ChatRepositoryImpl) {
    suspend fun execute(roomId: String, message: Message) = chatRepositoryImpl.sendMessage(roomId, message)
}
package com.vchat.domain.usecase.chats

import com.vchat.data.repository.ChatsRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class AddMessageToChatUseCase @Inject constructor(private val chatsRepositoryImpl: ChatsRepositoryImpl) {
    suspend fun execute(
        userId: String,
        roomId: String,
        message: String
    ) = chatsRepositoryImpl.addMessageToChat(userId, roomId, message)
}
package com.vchat.domain.usecase.chats

import com.vchat.data.models.Chat
import com.vchat.data.repository.ChatsRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class StartChatUseCase @Inject constructor(private val chatsRepositoryImpl: ChatsRepositoryImpl) {
    suspend fun execute(chat: Chat, friendId: String) = chatsRepositoryImpl.startChat(chat, friendId)
}
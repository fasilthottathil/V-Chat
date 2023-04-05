package com.vchat.domain.usecase.chats

import com.vchat.data.repository.ChatsRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 29/03/23.
 */
class GetChatsFromServerUseCase @Inject constructor(
    private val chatsRepositoryImpl: ChatsRepositoryImpl
) {
    suspend fun execute() = chatsRepositoryImpl.getChatsFromServer()
}
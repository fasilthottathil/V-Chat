package com.vchat.domain.usecase.chat

import android.net.Uri
import com.vchat.data.repository.ChatRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class UploadMessageImageUseCase @Inject constructor(private val chatRepositoryImpl: ChatRepositoryImpl) {
    suspend fun execute(
        roomId: String,
        id: String,
        imageUri: Uri
    ) = chatRepositoryImpl.uploadMessageImage(roomId, id, imageUri)
}
package com.vchat.domain.usecase.posts

import android.net.Uri
import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 06/04/23.
 */
class UploadPostImageUseCase @Inject constructor(private val postRepositoryImpl: PostRepositoryImpl) {
    suspend fun execute(postImageUri: Uri, postId: String) = postRepositoryImpl.uploadPostImage(postImageUri, postId)
}
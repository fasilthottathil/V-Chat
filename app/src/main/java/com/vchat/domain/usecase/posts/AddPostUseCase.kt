package com.vchat.domain.usecase.posts

import com.vchat.data.models.Post
import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 05/04/23.
 */
class AddPostUseCase @Inject constructor(private val postRepositoryImpl: PostRepositoryImpl) {
    suspend fun execute(post: Post) = postRepositoryImpl.insertPost(post)
}
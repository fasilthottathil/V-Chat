package com.vchat.domain.usecase.posts

import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 05/04/23.
 */
class GetPostByIdUseCase @Inject constructor(private val postRepositoryImpl: PostRepositoryImpl) {
    suspend fun execute(postId: String) = postRepositoryImpl.getPostById(postId)
}
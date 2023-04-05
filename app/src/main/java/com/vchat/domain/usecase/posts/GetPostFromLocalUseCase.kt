package com.vchat.domain.usecase.posts

import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 31/03/23.
 */
class GetPostFromLocalUseCase @Inject constructor(private val postRepositoryImpl: PostRepositoryImpl) {
    suspend fun execute() = postRepositoryImpl.getPostsFromLocal()
}
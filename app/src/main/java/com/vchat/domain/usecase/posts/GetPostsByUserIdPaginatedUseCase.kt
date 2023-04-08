package com.vchat.domain.usecase.posts

import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 07/04/23.
 */
class GetPostsByUserIdPaginatedUseCase @Inject constructor(private val postRepositoryImpl: PostRepositoryImpl) {
    suspend fun execute(nextPage: String?, userId: String) = postRepositoryImpl.getPostsByUserIdPaginated(nextPage, userId)
}
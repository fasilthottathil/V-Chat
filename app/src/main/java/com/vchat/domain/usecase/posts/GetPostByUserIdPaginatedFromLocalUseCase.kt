package com.vchat.domain.usecase.posts

import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 03/04/23.
 */
class GetPostByUserIdPaginatedFromLocalUseCase @Inject constructor(
    private val postRepositoryImpl: PostRepositoryImpl,
    private val getLastPostIdByUserIdUseCase: GetLastPostIdByUserIdUseCase,
    private val getPostsByUserIdPaginatedUseCase: GetPostsByUserIdPaginatedUseCase,
    private val upsertPostsUseCase: UpsertPostsUseCase
) {
    suspend fun execute(userId: String) = postRepositoryImpl.getPostsByUserIdPaginatedFromLocal(
        userId,
        getLastPostIdByUserIdUseCase,
        getPostsByUserIdPaginatedUseCase,
        upsertPostsUseCase
    )
}
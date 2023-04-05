package com.vchat.domain.usecase.posts

import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 03/04/23.
 */
class GetPostPaginatedFromLocalUseCase @Inject constructor(
    private val postRepositoryImpl: PostRepositoryImpl,
    private val getLastPostIdFromLocalUseCase: GetLastPostIdFromLocalUseCase,
    private val getPostsPaginatedUseCase: GetPostsPaginatedUseCase,
    private val upsertPostsUseCase: UpsertPostsUseCase
    ) {
    suspend fun execute(searchQuery: String?) = postRepositoryImpl.getPostsPaginated(
        getLastPostIdFromLocalUseCase,
        getPostsPaginatedUseCase,
        upsertPostsUseCase,
        searchQuery
    )
}
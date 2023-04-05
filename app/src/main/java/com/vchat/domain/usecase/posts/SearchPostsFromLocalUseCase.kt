package com.vchat.domain.usecase.posts

import com.vchat.data.repository.PostRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 03/04/23.
 */
class SearchPostsFromLocalUseCase @Inject constructor(private val postRepositoryImpl: PostRepositoryImpl) {
    suspend fun execute(searchQuery: String) = postRepositoryImpl.searchPost(searchQuery)
}
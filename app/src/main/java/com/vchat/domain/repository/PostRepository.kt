package com.vchat.domain.repository

import androidx.paging.PagingData
import com.vchat.common.Response
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.models.Post
import com.vchat.domain.usecase.posts.GetLastPostIdFromLocalUseCase
import com.vchat.domain.usecase.posts.GetPostsPaginatedUseCase
import com.vchat.domain.usecase.posts.SearchPostsFromLocalUseCase
import com.vchat.domain.usecase.posts.UpsertPostsUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 19/03/23.
 */
interface PostRepository {
    suspend fun getPostsFromServer(): Response<Unit>
    suspend fun getPostsFromLocal(): Flow<List<PostEntity>>
    suspend fun insertPost(post: Post): Response<PostEntity>
    suspend fun deletePost(postEntity: PostEntity): Response<Unit>
    suspend fun getPostFromServerPaginated(nextPage: String?): Response<List<PostEntity>>
    suspend fun getLastPostId(): String?
    suspend fun getPostsPaginated(
        getLastPostIdFromLocalUseCase: GetLastPostIdFromLocalUseCase,
        getPostsPaginatedUseCase: GetPostsPaginatedUseCase,
        upsertPostsUseCase: UpsertPostsUseCase,
        searchQuery: String?
    ): Flow<PagingData<PostEntity>>

    suspend fun upsertPosts(postEntityList: List<PostEntity>)
    suspend fun searchPost(searchQuery: String): List<PostEntity>
    suspend fun getPostById(postId: String): PostEntity?
}
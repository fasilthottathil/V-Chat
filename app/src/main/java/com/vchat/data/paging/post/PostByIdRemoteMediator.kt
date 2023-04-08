package com.vchat.data.paging.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vchat.common.Response
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.domain.usecase.posts.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Fasil on 03/04/23.
 */
@OptIn(ExperimentalPagingApi::class)
class PostByIdRemoteMediator @Inject constructor(
    private val userId: String,
    private val getLastPostIdByUserIdUseCase: GetLastPostIdByUserIdUseCase,
    private val getPostsByUserIdPaginatedUseCase: GetPostsByUserIdPaginatedUseCase,
    private val upsertPostsUseCase: UpsertPostsUseCase
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                withContext(Dispatchers.IO) {
                    getLastPostIdByUserIdUseCase.execute(userId)
                }
            }
        }
        return try {
            val response = withContext(Dispatchers.IO) {
                getPostsByUserIdPaginatedUseCase.execute(
                    page,
                    userId
                )
            }
            val endOfPaginationReached: Boolean
            if (response is Response.Success) {
                upsertPostsUseCase.upsertPosts(response.data)
                endOfPaginationReached = response.data.isEmpty()
            } else {
                return MediatorResult.Error(IllegalArgumentException((response as Response.Error).message))
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
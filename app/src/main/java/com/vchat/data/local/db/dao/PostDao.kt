package com.vchat.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.vchat.data.local.db.entity.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 19/03/23.
 */
@Dao
interface PostDao {
    @Upsert
    suspend fun upsertPost(postEntity: PostEntity)

    @Upsert
    suspend fun upsertPosts(postEntityList: List<PostEntity>)

    @Query("SELECT * FROM posts")
    fun getPosts(): Flow<List<PostEntity>>

    @Delete
    suspend fun deletePost(postEntity: PostEntity)

    @Query("SELECT id FROM posts WHERE ROWID = (SELECT MAX(ROWID) FROM POSTS)")
    fun getLastPostId(): String?

    @Query("SELECT * FROM posts ORDER BY createdOn DESC")
    fun getPostsPaginated(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY createdOn DESC")
    fun getPostsByUserIdPaginated(userId: String): PagingSource<Int, PostEntity>

    @Query("SELECT id FROM posts WHERE ROWID = (SELECT MAX(ROWID) AND userId = :userId FROM POSTS)")
    fun getLastPostIdByUserId(userId: String): String?

    @Query("SELECT * FROM posts WHERE content LIKE '%' || :searchQuery || '%'")
    fun getPostsSearchPaginated(searchQuery: String): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM posts WHERE content LIKE '%' || :searchQuery || '%'")
    fun searchPosts(searchQuery: String): List<PostEntity>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: String): PostEntity?

    @Query("DELETE FROM posts")
    fun deleteAll()

}
package com.vchat.data.repository

import android.content.res.Resources
import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vchat.R
import com.vchat.common.Constants
import com.vchat.common.Response
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.models.Post
import com.vchat.data.paging.post.PostRemoteMediator
import com.vchat.domain.repository.PostRepository
import com.vchat.domain.usecase.posts.GetLastPostIdFromLocalUseCase
import com.vchat.domain.usecase.posts.GetPostsPaginatedUseCase
import com.vchat.domain.usecase.posts.UpsertPostsUseCase
import com.vchat.utils.mapObjectTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Fasil on 19/03/23.
 */
class PostRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val appDatabase: AppDatabase,
    private val resources: Resources
) : PostRepository {
    override suspend fun getPostsFromServer(): Response<Unit> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.POSTS)
                .limit(Constants.POST_FETCH_LIMIT)
                .get()
                .await()
        }.onSuccess {
            it?.toObjects(Post::class.java)?.mapObjectTo<List<Post>, List<PostEntity>>()?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    appDatabase.postDao().upsertPosts(it)
                }
                return Response.Success(Unit)
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getPostsFromLocal(): Flow<List<PostEntity>> {
        return appDatabase.postDao().getPosts()
    }

    override suspend fun insertPost(post: Post): Response<PostEntity> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.POSTS).add(post).await()
        }.onSuccess {
            post.mapObjectTo<Post, PostEntity>()?.let {
                appDatabase.postDao().upsertPost(it)
                return Response.Success(it)
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun deletePost(postEntity: PostEntity): Response<Unit> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.POSTS)
                .whereEqualTo("id", postEntity.id)
                .limit(1)
                .get()
                .await()
        }.onSuccess {
            if (!it.isEmpty) {
                kotlin.runCatching {
                    return@runCatching firebaseFirestore.collection(Constants.POSTS)
                        .document(it.documents[0].id)
                        .delete()
                        .await()
                }.onSuccess {
                    CoroutineScope(Dispatchers.IO).launch {
                        appDatabase.postDao().deletePost(postEntity)
                    }
                    return Response.Success(Unit)
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    return Response.Error(throwable.message.toString())
                }
            } else {
                return Response.Error(resources.getString(R.string.post_not_found))
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getPostFromServerPaginated(nextPage: String?): Response<List<PostEntity>> {
        kotlin.runCatching {
            return@runCatching if (nextPage == null) {
                firebaseFirestore.collection(Constants.POSTS)
                    .limit(20)
                    .get()
                    .await()
            } else {
                firebaseFirestore.collection(Constants.POSTS)
                    .limit(20)
                    .orderBy("id")
                    .startAfter(nextPage)
                    .get()
                    .await()
            }
        }.onSuccess {
            val chatEntityList = mutableListOf<PostEntity>()
            it?.toObjects(Post::class.java)?.mapObjectTo<List<Post>, List<PostEntity>>()
                ?.let { entities ->
                    CoroutineScope(Dispatchers.IO).launch {
                        appDatabase.postDao().upsertPosts(entities)
                    }
                    chatEntityList.addAll(entities)
                    return Response.Success(chatEntityList)
                }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getLastPostId(): String? {
        return appDatabase.postDao().getLastPostId()
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPostsPaginated(
        getLastPostIdFromLocalUseCase: GetLastPostIdFromLocalUseCase,
        getPostsPaginatedUseCase: GetPostsPaginatedUseCase,
        upsertPostsUseCase: UpsertPostsUseCase,
        searchQuery: String?
    ): Flow<PagingData<PostEntity>> {
        val pagingSourceFactory = {
            if (searchQuery.isNullOrEmpty()) appDatabase.postDao()
                .getPostsPaginated() else appDatabase.postDao().getPostsSearchPaginated(searchQuery)
        }
        return Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 20),
            remoteMediator = PostRemoteMediator(
                getLastPostIdFromLocalUseCase,
                getPostsPaginatedUseCase,
                upsertPostsUseCase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun upsertPosts(postEntityList: List<PostEntity>) {
        appDatabase.postDao().upsertPosts(postEntityList)
    }

    override suspend fun searchPost(searchQuery: String): List<PostEntity> {
        return appDatabase.postDao().searchPosts(searchQuery)
    }

    override suspend fun getPostById(postId: String): PostEntity? {
        return appDatabase.postDao().getPostById(postId)
    }

    override suspend fun updatePost(post: Post): Response<PostEntity> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.POSTS)
                .whereEqualTo("id", post.id)
                .limit(1)
                .get()
                .await()
        }.onSuccess {
            if (!it.isEmpty) {
                kotlin.runCatching {
                    return@runCatching firebaseFirestore.collection(Constants.POSTS)
                        .document(it.documents[0].id)
                        .update(post.toMap())
                        .await()
                }.onSuccess {
                    post.mapObjectTo<Post, PostEntity>()?.let { postEntity ->
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.postDao().upsertPost(postEntity)
                        }
                        return Response.Success(postEntity)
                    }
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    return Response.Error(throwable.message.toString())
                }
            } else {
                return Response.Error(resources.getString(R.string.post_not_found))
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun uploadPostImage(postImageUri: Uri, postId: String): Response<String> {
        val storageReference = firebaseStorage.reference.child(Constants.POSTS + "/" + postId)
        kotlin.runCatching {
            return@runCatching storageReference.putFile(postImageUri).await()
        }.onSuccess {
            kotlin.runCatching {
                return@runCatching storageReference.downloadUrl.await()
            }.onSuccess {
                return Response.Success(it.toString())
            }.onFailure { throwable ->
                Timber.e(throwable)
                return Response.Error(throwable.message.toString())
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }
}
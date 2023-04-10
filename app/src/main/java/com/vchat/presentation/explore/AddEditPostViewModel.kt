package com.vchat.presentation.explore

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.common.enums.PostType
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.data.models.Post
import com.vchat.domain.usecase.posts.AddPostUseCase
import com.vchat.domain.usecase.posts.GetPostByIdUseCase
import com.vchat.domain.usecase.posts.UpdatePostUseCase
import com.vchat.domain.usecase.posts.UploadPostImageUseCase
import com.vchat.domain.usecase.user.GetUserByIdFromLocalUseCase
import com.vchat.utils.mapObjectTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Fasil on 05/04/23.
 */
@HiltViewModel
class AddEditPostViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val uploadPostImageUseCase: UploadPostImageUseCase,
    private val getUserByIdFromLocalUseCase: GetUserByIdFromLocalUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _post: MutableStateFlow<PostEntity?> = MutableStateFlow(null)
    val post get() = _post.asStateFlow()
    private val _onProductAddOrUpdated: MutableState<Boolean> = mutableStateOf(false)
    val onProductAddOrUpdated: State<Boolean> get() = _onProductAddOrUpdated

    fun getPostById(postId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            postId?.let {
                _post.emit(getPostByIdUseCase.execute(it))
            }
        }
    }

    fun addOrEditPost(postEntity: PostEntity, imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true

            val post = getPostByIdUseCase.execute(postEntity.id)

            if (post == null || post.id.isEmpty()) {
                val user = getUserByIdFromLocalUseCase.execute(appPreferenceManager.getMyId().toString())
                postEntity.apply {
                    id = UUID.randomUUID().toString()
                    postType = if (imageUri == null) PostType.TEXT.name else PostType.IMAGE.name
                    username = user?.name.toString()
                    userId = user?.id.toString()
                    profileUrl = user?.profileUrl.toString()
                }
                if (postEntity.postType == PostType.TEXT.name && postEntity.content.isEmpty()) {
                    stopLoading()
                    _error.value = "Enter post content"
                    return@launch
                }
            } else {
                postEntity.postType = if (imageUri == null && post.postImageUrl.isEmpty()) PostType.TEXT.name else PostType.IMAGE.name
                if (postEntity.postType == PostType.TEXT.name && postEntity.content.isEmpty()) {
                    stopLoading()
                    _error.value = "Enter post content"
                    return@launch
                }
            }

            if (imageUri != null) {
                when(val response = uploadPostImageUseCase.execute(imageUri, postEntity.id)) {
                    is Response.Success -> {
                        postEntity.postImageUrl = response.data
                    }
                    is Response.Error -> {
                        stopLoading()
                        _error.value = response.message
                        return@launch
                    }
                }
            }

            if (post == null || post.id.isEmpty()) {
                postEntity.mapObjectTo<PostEntity, Post>()?.let {
                    when (val response = addPostUseCase.execute(it)) {
                        is Response.Success -> {
                            stopLoading()
                            _onProductAddOrUpdated.value = true
                        }
                        is Response.Error -> {
                            stopLoading()
                            _error.value = response.message
                        }
                    }
                }
            } else {
                postEntity.mapObjectTo<PostEntity, Post>()?.let {
                    when (val response = updatePostUseCase.execute(it)) {
                        is Response.Success -> {
                            stopLoading()
                            _onProductAddOrUpdated.value = true
                        }
                        is Response.Error -> {
                            stopLoading()
                            _error.value = response.message
                        }
                    }
                }
            }
        }
    }

    fun resetError() {
        _error.value = null
    }

    private fun stopLoading() {
        _loading.value = false
    }
}
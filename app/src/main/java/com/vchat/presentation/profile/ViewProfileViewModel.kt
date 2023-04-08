package com.vchat.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vchat.common.Response
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.domain.usecase.posts.DeletePostUseCase
import com.vchat.domain.usecase.posts.GetPostByUserIdPaginatedFromLocalUseCase
import com.vchat.domain.usecase.user.GetUserFromLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 07/04/23.
 */
@HiltViewModel
class ViewProfileViewModel @Inject constructor(
    appPreferenceManager: AppPreferenceManager,
    private val getPostByUserIdPaginatedFromLocalUseCase: GetPostByUserIdPaginatedFromLocalUseCase,
    private val getUserFromLocalUseCase: GetUserFromLocalUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _posts: MutableStateFlow<PagingData<PostEntity>> = MutableStateFlow(PagingData.empty())
    val posts get() = _posts.asStateFlow()
    val userID = appPreferenceManager.getMyId()
    private val _user: MutableStateFlow<UserEntity> = MutableStateFlow(UserEntity())
    val user get() = _user.asStateFlow()

    fun getPosts(userId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                getPostByUserIdPaginatedFromLocalUseCase.execute(userId).collectLatest {
                    _posts.emit(it)
                }
            }
        }
    }

    fun getUser(userId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                getUserFromLocalUseCase.execute(userId).collectLatest {
                    it?.let { _user.emit(it) }
                }
            }
        }
    }

    fun deletePost(postEntity: PostEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            when(val response = deletePostUseCase.execute(postEntity)) {
                is Response.Success -> {
                    stopLoading()
                }
                is Response.Error -> {
                    stopLoading()
                    _error.value = response.message
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
package com.vchat.presentation.explore

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.vchat.common.Response
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.domain.usecase.posts.DeletePostUseCase
import com.vchat.domain.usecase.posts.GetPostPaginatedFromLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 31/03/23.
 */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    appPreferenceManager: AppPreferenceManager,
    private val getPostPaginatedFromLocalUseCase: GetPostPaginatedFromLocalUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val searchQuery = MutableStateFlow("")


    @OptIn(ExperimentalCoroutinesApi::class)
    val postPaginated = searchQuery.flatMapLatest {
        getPostPaginatedFromLocalUseCase.execute(it).cachedIn(viewModelScope)
    }

    val userID = appPreferenceManager.getMyId()

    fun search(searchQuery: String) {
        this.searchQuery.value = searchQuery
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

    private fun stopLoading() {
        _loading.value = false
    }

    fun resetError() {
        _error.value = null
    }

}
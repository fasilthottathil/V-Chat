package com.vchat.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.domain.usecase.posts.GetPostByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 05/04/23.
 */
@HiltViewModel
class AddEditPostViewModel @Inject constructor(
    private val getPostByIdUseCase: GetPostByIdUseCase
): ViewModel() {
    private val _post: MutableStateFlow<PostEntity?> = MutableStateFlow(null)
    val post get() = _post.asStateFlow()

    fun getPostById(postId: String?) {
        viewModelScope.launch(Dispatchers.IO) { 
           postId?.let {
               _post.emit(getPostByIdUseCase.execute(it))
           }
        }
    }
}
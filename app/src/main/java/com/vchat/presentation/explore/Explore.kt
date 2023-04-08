package com.vchat.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.vchat.common.enums.PostDialogOption
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.presentation.components.*
import com.vchat.ui.theme.Primary
import kotlinx.coroutines.flow.flow

/**
 * Created by Fasil on 19/03/23.
 */
@Composable
fun Explore(
    modifier: Modifier,
    error: State<String?>,
    loading: State<Boolean>,
    resetError: () -> Unit,
    pagingItems: LazyPagingItems<PostEntity>,
    userID: String?,
    searchPost: (String) -> Unit,
    deletePost: (PostEntity) -> Unit,
    editPost: (PostEntity) -> Unit,
    addPost: () -> Unit,
    viewProfile: (String) -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { addPost() },
            shape = CircleShape,
            backgroundColor = Color.Black
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
        }
    }, modifier = modifier, floatingActionButtonPosition = FabPosition.End) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                SearchBox { searchValue -> searchPost(searchValue) }
            }
            val showPostOptionDialog = remember {
                mutableStateOf(false)
            }

            val showErrorMessage = remember {
                mutableStateOf(false)
            }

            showErrorMessage.value = error.value != null

            if (showErrorMessage.value) {
                MessageDialog(message = error.value.toString()) { resetError() }
            }

            if (loading.value) {
                LoadingDialog()
            }

            val post = remember {
                mutableStateOf(PostEntity())
            }
            LazyColumn(contentPadding = PaddingValues(4.dp)) {
                items(
                    items = pagingItems.itemSnapshotList.items.distinct(),
                    key = { entity -> entity.id }
                ) { postEntity ->
                    if (showPostOptionDialog.value) {
                        PostOptionDialog(
                            userId = userID,
                            postUserId = post.value.userId
                        ) { option ->
                            showPostOptionDialog.value = false
                            when (option) {
                                PostDialogOption.EDIT -> editPost(post.value)
                                PostDialogOption.DELETE -> deletePost(post.value)
                                PostDialogOption.VIEW_PROFILE -> viewProfile(post.value.userId)
                                else -> {}
                            }
                        }
                    }
                    PostItem(postEntity = postEntity, onMoreClick = { entity ->
                        showPostOptionDialog.value = true
                        post.value = entity
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
                when (pagingItems.loadState.refresh) {
                    is LoadState.NotLoading -> Unit
                    is LoadState.Loading -> {}
                    is LoadState.Error -> {}
                }
            }
        }
    }
}

@Preview
@Composable
fun ExplorePreview() {
    val pagingItems = flow<PagingData<PostEntity>> { }
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    Explore(
        modifier = Modifier,
        error, loading, {}, pagingItems = pagingItems.collectAsLazyPagingItems(),
        userID = "", {}, {}, {}, {}, {}
    )
}
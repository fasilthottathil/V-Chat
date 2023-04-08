package com.vchat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.vchat.common.enums.PostDialogOption
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.presentation.components.*
import com.vchat.ui.theme.Primary
import kotlinx.coroutines.flow.flow

/**
 * Created by Fasil on 06/04/23.
 */
@Composable
fun ViewProfile(
    error: State<String?>,
    loading: State<Boolean>,
    resetError: () -> Unit,
    pagingItems: LazyPagingItems<PostEntity>,
    userID: String?,
    user: State<UserEntity>,
    deletePost: (PostEntity) -> Unit,
    editPost: (PostEntity) -> Unit,
    startChat: (UserEntity) -> Unit,
    editProfile: (UserEntity) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        TopAppBar(backgroundColor = Color.White) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Profile", fontSize = 16.sp, color = Color.Black)
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary),
            contentPadding = PaddingValues(4.dp)
        ) {
            item {
                ProfileCard(
                    userEntity = user.value,
                    userId = userID,
                ) {
                    if (it) {
                        editProfile(user.value)
                    } else {
                        startChat(user.value)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

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

@Preview
@Composable
fun ViewProfilePreview() {
    val pagingItems = flow<PagingData<PostEntity>> { }
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val user = remember {
        mutableStateOf(UserEntity())
    }
    ViewProfile(
        error,
        loading,
        {},
        pagingItems = pagingItems.collectAsLazyPagingItems(),
        userID = "",
        user = user,
        {}, {}, {}, {}
    )
}
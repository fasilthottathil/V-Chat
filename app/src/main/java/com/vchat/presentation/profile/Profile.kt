package com.vchat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vchat.common.enums.PostType
import com.vchat.data.local.db.entity.PostEntity
import com.vchat.presentation.components.PostItem
import com.vchat.presentation.components.ProfileCard
import com.vchat.ui.theme.Primary

/**
 * Created by Fasil on 21/03/23.
 */
@Composable
fun Profile() {
    val scrollState = rememberScrollState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        item {
            ProfileCard()
        }

        items(10) { pos ->
            Spacer(modifier = Modifier.height(12.dp))
            PostItem(
                PostEntity(
                    username = "Username",
                    profileUrl = "https://avatars.githubusercontent.com/u/59242329?v=4",
                    postType = PostType.IMAGE.name,
                    content = "Test post",
                    postImageUrl = "https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg"
                )
            ){}
            if (pos == 9) {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }


    }

}

@Preview
@Composable
fun ProfilePreview() {
    Profile()
}
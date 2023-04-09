package com.vchat.presentation.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.presentation.components.ChatItem
import com.vchat.ui.theme.Primary

/**
 * Created by Fasil on 19/03/23.
 */
@Composable
fun Chats(
    modifier: Modifier,
    user: State<UserEntity?>,
    chats: State<List<ChatEntity>?>,
    onClickSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
    ) {
        Row(
            modifier = Modifier
                .height(55.dp)
                .background(Color.White)
                .padding(start = 12.dp, end = 12.dp, top = 3.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(ShapeDefaults.Medium)
                    .background(Color.White)
            ) {
                val painter = rememberAsyncImagePainter(user.value?.profileUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(1f)
                    .padding(end = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Chats",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Image(
                imageVector = Icons.Default.Settings,
                colorFilter = ColorFilter.tint(color = Color.Black),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClickSettings.invoke() }
            )
        }
        LazyColumn(modifier = modifier.padding(start = 4.dp, end = 4.dp)) {
            chats.value?.let {
                items(it) { chatEntity ->
                    Spacer(modifier = Modifier.height(12.dp))
                    ChatItem(chatEntity) {

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatsPreview() {
    val userState = remember {
        mutableStateOf(UserEntity(profileUrl = "https://avatars.githubusercontent.com/u/59242329?v=4"))
    }
    val chats = remember {
        mutableStateOf(emptyList<ChatEntity>())
    }
    Chats(modifier = Modifier, user = userState, chats = chats) {}
}
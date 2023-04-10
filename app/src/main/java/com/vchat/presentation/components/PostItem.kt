package com.vchat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vchat.common.enums.PostType
import com.vchat.data.local.db.entity.PostEntity

/**
 * Created by Fasil on 19/03/23.
 */
@Composable
fun PostItem(postEntity: PostEntity, onMoreClick: (PostEntity) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .background(color = Color.White)
            .border(width = 1.dp, color = Color.Black, shape = ShapeDefaults.Medium)
            .clip(ShapeDefaults.Medium)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape)
            ) {
                val painter =
                    rememberAsyncImagePainter(postEntity.profileUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = postEntity.username,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onMoreClick.invoke(postEntity) }
            )
        }

        Column(modifier = Modifier.padding(start = 6.dp, end = 6.dp, bottom = 6.dp)) {
            when (postEntity.postType) {
                PostType.TEXT.name -> {
                    Text(text = postEntity.content, fontSize = 16.sp, color = Color.Black)
                }
                PostType.IMAGE.name -> {
                    Text(text = postEntity.content, fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(3.dp))
                    val postImage = rememberAsyncImagePainter(
                        postEntity.postImageUrl,
                        filterQuality = FilterQuality.Low
                    )
                    Image(
                        painter = postImage,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(ShapeDefaults.Small)
                            .heightIn(min = 100.dp)
                    )
                }
                else -> throw IllegalArgumentException("Unknown Post Type")
            }
        }

    }
}

@Preview
@Composable
fun PostItemPreview() {
    PostItem(
        PostEntity(
            username = "Fasil",
            profileUrl = "https://avatars.githubusercontent.com/u/59242329?v=4",
            postType = PostType.TEXT.name,
            content = "Test post"
        )
    ){}
}
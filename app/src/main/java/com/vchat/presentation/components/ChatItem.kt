package com.vchat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.ui.theme.Primary
import com.vchat.ui.theme.Shapes
import com.vchat.utils.formatTimestamp

/**
 * Created by Fasil on 19/03/23.
 */
@Composable
fun ChatItem(chatEntity: ChatEntity, onClick: (ChatEntity) -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(Shapes.medium)
        .clickable { }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape.copy(
                        topStart = CornerSize(40.dp),
                        topEnd = CornerSize(40.dp),
                        bottomEnd = CornerSize(4.dp),
                        bottomStart = CornerSize(4.dp)
                    )
                )
                .height(86.dp)
                .background(Primary)
                .clip(Shapes.medium)
                .padding(start = 12.dp, end = 12.dp)
        ) {
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Black, shape = Shapes.medium)
                    .height(80.dp)
                    .background(Color.White)
                    .clip(Shapes.medium)
                    .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape)
                        .background(Color.White)
                ) {
                    val painter =
                        rememberAsyncImagePainter(chatEntity.profileUrl)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chatEntity.name,
                        color = Color.Black,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = chatEntity.message,
                        fontSize = 12.sp,
                        maxLines = 1,
                        fontStyle = FontStyle.Italic,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = chatEntity.timestamp.formatTimestamp(),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    if (chatEntity.messageCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(25.dp)
                                .clip(CircleShape)
                                .background(Primary)
                                .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (chatEntity.messageCount > 9) "9+" else chatEntity.messageCount.toString(),
                                Modifier.padding(4.dp),
                                color = Color.Black
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun ChatItemPreview() {
    ChatItem(
        ChatEntity(
            name = "Fasil",
            message = "Hello there",
            timestamp = System.currentTimeMillis()
        )
    ) {}
}

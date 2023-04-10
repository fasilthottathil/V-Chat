package com.vchat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Text
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.vchat.data.local.db.entity.MessageEntity
import com.vchat.ui.theme.Primary
import com.vchat.utils.formatTimestamp

/**
 * Created by Fasil on 09/04/23.
 */
@Composable
fun SendImageMessage(messageEntity: MessageEntity) {
    Column(
        Modifier
            .background(Primary)
            .fillMaxWidth()
            .padding(4.dp), horizontalAlignment = Alignment.End
    ) {
        val painter= rememberAsyncImagePainter(messageEntity.message)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = ShapeDefaults.Medium.copy(
                        topStart = CornerSize(4.dp),
                        topEnd = CornerSize(4.dp),
                        bottomStart = CornerSize(4.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                )
                .padding(6.dp),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = messageEntity.timestamp.formatTimestamp(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 8.sp
        )
    }
}

@Preview
@Composable
fun SendImageMessagePreview() {
    SendImageMessage(MessageEntity())
}
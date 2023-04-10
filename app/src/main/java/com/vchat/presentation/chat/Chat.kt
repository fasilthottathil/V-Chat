package com.vchat.presentation.chat

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.vchat.common.enums.MessageType
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.data.local.db.entity.MessageEntity
import com.vchat.presentation.components.*
import com.vchat.ui.theme.Primary
import com.vchat.utils.requestPermissions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

/**
 * Created by Fasil on 09/04/23.
 */
@Composable
fun Chat(
    error: State<String?>,
    loading: State<Boolean>,
    chatEntity: ChatEntity?,
    userId: String,
    onSendMessage: State<Boolean>,
    messages: State<List<MessageEntity>>,
    resetError: () -> Unit,
    sendTextMessage: (String) -> Unit,
    sendImageMessage: (Uri) -> Unit,
    onClickBack: () -> Unit
) {

    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { it?.let(sendImageMessage) }
    )

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

    val message = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .imePadding()
    ) {
        TopAppBar(backgroundColor = Color.White) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClickBack()
                    }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                val painter = rememberAsyncImagePainter(chatEntity?.profileUrl)
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = chatEntity?.name.orEmpty(),
                color = Color.Black,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Default.Attachment,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            imagePicker.launch("image/*")
                        } else {
                            requestPermissions(context as ComponentActivity)
                        }
                    }
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        val scrollState = rememberLazyListState()
        LaunchedEffect(messages.value) {
           snapshotFlow { messages.value }.filter { it.isNotEmpty() }.collectLatest {
               scrollState.animateScrollToItem(messages.value.lastIndex)
           }
        }

        LazyColumn(modifier = Modifier.weight(1f), state = scrollState) {
            items(messages.value.size) {
                val messageEntity = messages.value[it]
                if (messageEntity.messageType == MessageType.TEXT.name) {
                    if (userId == messageEntity.userId) {
                        SendTextMessage(messageEntity)
                    } else {
                        ReceiveTextMessage(messageEntity)
                    }
                } else if (messageEntity.messageType == MessageType.IMAGE.name) {
                    if (userId == messageEntity.userId) {
                        SendImageMessage(messageEntity)
                    } else {
                        ReceiveImageMessage(messageEntity)
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = message.value,
                placeholder = { Text(text = "Type message...") },
                onValueChange = { message.value = it },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(max = 200.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black
                ), maxLines = Int.MAX_VALUE
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable {
                        if (onSendMessage.value) {
                            sendTextMessage(message.value)
                            message.value = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatPreview() {
    val error = remember {
        mutableStateOf(null)
    }
    val loading = remember {
        mutableStateOf(false)
    }
    val onSendMessage = remember {
        mutableStateOf(true)
    }
    val messages = remember {
        mutableStateOf(listOf<MessageEntity>())
    }
    Chat(error, loading, ChatEntity(), "", onSendMessage, messages, {}, {}, {}) { }
}
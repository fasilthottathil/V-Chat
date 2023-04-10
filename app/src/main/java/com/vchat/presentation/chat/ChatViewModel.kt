package com.vchat.presentation.chat

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.common.enums.MessageType
import com.vchat.data.local.db.entity.MessageEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.data.models.Message
import com.vchat.domain.usecase.chat.GetMessagesFromLocalUseCase
import com.vchat.domain.usecase.chat.GetMessagesUseCase
import com.vchat.domain.usecase.chat.SendMessageUseCase
import com.vchat.domain.usecase.chat.UploadMessageImageUseCase
import com.vchat.domain.usecase.chats.AddMessageToChatUseCase
import com.vchat.domain.usecase.chats.ClearMessageCountUseCase
import com.vchat.domain.usecase.chats.IncrementCountAndUpdateMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    appPreferenceManager: AppPreferenceManager,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getMessagesFromLocalUseCase: GetMessagesFromLocalUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addMessageToChatUseCase: AddMessageToChatUseCase,
    private val clearMessageCountUseCase: ClearMessageCountUseCase,
    private val incrementCountAndUpdateMessageUseCase: IncrementCountAndUpdateMessageUseCase,
    private val uploadMessageImageUseCase: UploadMessageImageUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _messages: MutableStateFlow<List<MessageEntity>> = MutableStateFlow(listOf())
    val messages get() = _messages.asStateFlow()
    val userId = appPreferenceManager.getMyId()
    var roomId: String? = null
    private val _onSendMessage: MutableState<Boolean> = mutableStateOf(true)
    val onSendMessage: State<Boolean> get() = _onSendMessage

    fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            roomId?.let {
                clearMessageCount()
                val messagesFromLocal = async {
                    getMessagesFromLocalUseCase.execute(it).collectLatest {
                        _messages.emit(it)
                    }
                }
                val messagesFromServer = async { getMessagesUseCase.execute(it) }
                messagesFromLocal.await()
                messagesFromServer.await()
            }
        }
    }

    private fun clearMessageCount() {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                roomId?.let {
                    clearMessageCountUseCase.execute(userId, it)
                }
            }
        }
    }

    fun sendMessage(chatMessage: String?, imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val message = Message().apply {
                this.id = UUID.randomUUID().toString()
                this.roomId = this@ChatViewModel.roomId.orEmpty()
                this.userId = this@ChatViewModel.userId.toString()
                this.messageType = if (imageUri == null) MessageType.TEXT.name else MessageType.IMAGE.name
            }
            if (imageUri == null) {
                if (chatMessage.isNullOrEmpty()) {
                    _error.value = "Enter message"
                    return@launch
                }
                message.message = chatMessage.toString()
                roomId?.let {
                    _onSendMessage.value = false
                    when (sendMessageUseCase.execute(it, message)) {
                        is Response.Success -> {
                            _onSendMessage.value = true
                        }
                        is Response.Error -> {
                            _onSendMessage.value = true
                        }
                    }
                }
            } else {
                roomId?.let {
                    _onSendMessage.value = false
                    _loading.value = true
                    when (val response = uploadMessageImageUseCase.execute(it, message.id, imageUri)) {
                        is Response.Success -> {
                            message.message = response.data
                            when (sendMessageUseCase.execute(it, message)) {
                                is Response.Success -> {
                                    stopLoading()
                                    _onSendMessage.value = false
                                }
                                is Response.Error -> {
                                    stopLoading()
                                    _onSendMessage.value = false
                                }
                            }
                        }
                        is Response.Error -> {
                            stopLoading()
                            _onSendMessage.value = false
                            _error.value = response.message
                        }
                    }
                }
            }
        }
    }

    fun resetError() {
        _error.value = null
    }

    private fun stopLoading() {
        _loading.value = false
    }
}
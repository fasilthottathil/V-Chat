package com.vchat.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.domain.usecase.chats.GetChatsFromLocalUseCase
import com.vchat.domain.usecase.chats.GetChatsFromServerUseCase
import com.vchat.domain.usecase.user.GetUserFromLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 26/03/23.
 */
@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val getUserFromLocalUseCase: GetUserFromLocalUseCase,
    private val getChatsFromLocalUseCase: GetChatsFromLocalUseCase,
    private val getChatsFromServerUseCase: GetChatsFromServerUseCase,
    appPreferenceManager: AppPreferenceManager
) : ViewModel() {

    private val _user: MutableStateFlow<UserEntity?> = MutableStateFlow(null)
    val user get() = _user.asStateFlow()
    private val _chats: MutableStateFlow<List<ChatEntity>?> = MutableStateFlow(null)
    val chats get() = _chats.asStateFlow()

    init {
        getUserById(appPreferenceManager.getMyId())
        viewModelScope.launch (Dispatchers.IO) {
            val chatsLocal = async { getChatsFromLocal(appPreferenceManager.getMyId()) }
            val chatFromServer = async { getChatsFromServerUseCase.execute() }
            chatsLocal.await()
            chatFromServer.await()
        }
    }

    private fun getChatsFromLocal(myId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            myId?.let {
                getChatsFromLocalUseCase.execute().collectLatest {
                    _chats.emit(it)
                }
            }
        }
    }

    private fun getUserById(myId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            myId?.let {
                getUserFromLocalUseCase.execute(myId).collectLatest {
                    _user.emit(it)
                }
            }
        }
    }

}
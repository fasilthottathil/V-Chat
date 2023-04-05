package com.vchat.data.repository

import android.content.res.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.vchat.R
import com.vchat.common.Constants
import com.vchat.common.Response
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.db.entity.ChatEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.data.models.Chat
import com.vchat.domain.repository.ChatsRepository
import com.vchat.utils.mapObjectTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Fasil on 26/03/23.
 */
class ChatsRepositoryImpl @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val appDatabase: AppDatabase,
    private val firebaseFirestore: FirebaseFirestore,
    private val resources: Resources
) : ChatsRepository {
    override suspend fun getChatsFromServer() {
        kotlin.runCatching {
            firebaseFirestore.collection(Constants.CHATS)
                .document(appPreferenceManager.getMyId().toString())
                .collection(Constants.CHATS)
                .addSnapshotListener { value, error ->
                    if (error != null) return@addSnapshotListener
                    value?.toObjects(Chat::class.java)?.let {
                        val chatEntityList = mutableListOf<ChatEntity>()

                        it.filterNotNull().mapObjectTo<List<Chat>, List<ChatEntity>>()?.let { chatEntities ->
                            chatEntityList.addAll(chatEntities)
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            appDatabase.chatsDao().upsertChats(chatEntityList)
                        }
                    }
                }
        }.onFailure {
            Timber.e(it)
        }
    }

    override suspend fun startChat(chat: Chat, friendId: String): Response<ChatEntity> {
        val chatEntity = appDatabase.chatsDao().getChatByEmail(chat.email)
        kotlin.runCatching {
            if (chatEntity == null) {
                return@runCatching firebaseFirestore.collection(Constants.CHATS)
                    .document(appPreferenceManager.getMyId().toString())
                    .collection(Constants.CHATS)
                    .add(chat)
                    .await()
            } else {
                return Response.Success(chatEntity)
            }
        }.onSuccess {
            val myData = appDatabase.userDao().getUserById(appPreferenceManager.getMyId().toString()) ?: return Response.Error(resources.getString(R.string.something_went_wrong))
            kotlin.runCatching {
                return@runCatching firebaseFirestore.collection(Constants.CHATS)
                    .document(friendId)
                    .collection(Constants.CHATS)
                    .add(chat.copy(email = myData.email, name = myData.name, profileUrl = myData.profileUrl))
                    .await()
            }.onSuccess {
                chat.mapObjectTo<Chat, ChatEntity>()?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        appDatabase.chatsDao().upsertChat(it)
                    }
                    return Response.Success(it)
                }
            }.onFailure {
                Timber.e(it)
                return Response.Error(it.message.toString())
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getChatsFromLocal(): Flow<List<ChatEntity>> {
        return appDatabase.chatsDao().getChats()
    }
}
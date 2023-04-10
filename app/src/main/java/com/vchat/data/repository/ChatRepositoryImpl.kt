package com.vchat.data.repository

import android.content.res.Resources
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vchat.R
import com.vchat.common.Constants
import com.vchat.common.Response
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.db.entity.MessageEntity
import com.vchat.data.models.Message
import com.vchat.domain.repository.ChatRepository
import com.vchat.utils.mapObjectTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class ChatRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val resources: Resources
) : ChatRepository {
    override suspend fun getMessages(roomId: String) {
        kotlin.runCatching {
            firebaseFirestore.collection(Constants.MESSAGES)
                .document(roomId)
                .collection(Constants.MESSAGES)
                .addSnapshotListener { value, error ->
                    if (error != null) return@addSnapshotListener
                    value?.toObjects(Message::class.java)?.let {
                        it.mapObjectTo<List<Message>, List<MessageEntity>>()?.let {
                            CoroutineScope(Dispatchers.IO).launch {
                                appDatabase.chatDao().upsertMessages(it)
                            }
                        }
                    }
                }
        }.onFailure {
            Timber.e(it)
        }
    }

    override suspend fun sendMessage(roomId: String, message: Message): Response<MessageEntity> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.MESSAGES)
                .document(roomId)
                .collection(Constants.MESSAGES)
                .add(message)
                .await()
        }.onSuccess {
            message.mapObjectTo<Message, MessageEntity>()?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    appDatabase.chatDao().upsertMessage(it)
                }
                return Response.Success(it)
            }

        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.orEmpty())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun deleteMessage(
        roomId: String,
        messageEntity: MessageEntity
    ): Response<MessageEntity> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.MESSAGES)
                .document(roomId)
                .collection(Constants.MESSAGES)
                .whereEqualTo("id", messageEntity.id)
                .limit(1)
                .get()
                .await()
        }.onSuccess {
            if (it.isEmpty) {
                return Response.Error(resources.getString(R.string.message_not_found))
            } else {
                kotlin.runCatching {
                    return@runCatching firebaseFirestore.collection(Constants.MESSAGES)
                        .document(roomId)
                        .collection(Constants.MESSAGES)
                        .document(it.documents[0].id)
                        .delete()
                        .await()
                }.onSuccess {
                    CoroutineScope(Dispatchers.IO).launch {
                        appDatabase.chatDao().deleteMessage(messageEntity)
                    }
                    return Response.Success(messageEntity)
                }.onFailure { throwable ->
                    Timber.e(throwable)
                    return Response.Error(throwable.message.orEmpty())
                }
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.orEmpty())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getMessagesFromLocal(roomId: String): Flow<List<MessageEntity>> {
        return appDatabase.chatDao().getMessages(roomId)
    }

    override suspend fun uploadMessageImage(
        roomId: String,
        id: String,
        imageUri: Uri
    ): Response<String> {
        val storageReference = firebaseStorage.reference.child(Constants.MESSAGES + "/$roomId/$id")
        kotlin.runCatching {
            return@runCatching storageReference.putFile(imageUri).await()
        }.onSuccess {
            kotlin.runCatching {
                return@runCatching storageReference.downloadUrl.await()
            }.onSuccess { uri ->
                return Response.Success(uri.toString())
            }.onFailure {
                Timber.e(it)
                return Response.Error(it.message.orEmpty())
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.orEmpty())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }
}
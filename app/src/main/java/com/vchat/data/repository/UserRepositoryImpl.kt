package com.vchat.data.repository

import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vchat.R
import com.vchat.common.Constants
import com.vchat.common.Response
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.data.models.User
import com.vchat.domain.repository.UserRepository
import com.vchat.utils.mapObjectTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Fasil on 12/03/23.
 */
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
    private val appDatabase: AppDatabase,
    private val resources: Resources
) : UserRepository {
    override suspend fun login(email: String, password: String): Response<UserEntity> {
        kotlin.runCatching {
            return@runCatching firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }.onSuccess {
            return if (it.user == null) {
                Response.Error(resources.getString(R.string.invalid_username_or_pass))
            } else {
                when(val response = getUserFromServer(email)) {
                    is Response.Success -> Response.Success(response.data)
                    is Response.Error -> Response.Error(response.message)
                }
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun sendPasswordResetEmail(email: String): Response<Unit> {
        kotlin.runCatching {
            return@runCatching firebaseAuth.sendPasswordResetEmail(email).await()
        }.onSuccess { return Response.Success(Unit) }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun register(user: User): Response<UserEntity> {
        kotlin.runCatching {
            return@runCatching firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
        }.onSuccess {
            if (it.user != null) {
                return when(val response = createUser(user)) {
                    is Response.Success -> {
                        Response.Success(response.data)
                    }
                    is Response.Error -> {
                        Response.Error(response.message)
                    }
                }
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun createUser(user: User): Response<UserEntity> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.USERS).add(user).await()
        }.onSuccess {
            user.mapObjectTo<User, UserEntity>()?.let {
                appDatabase.userDao().upsertUser(it)
                return Response.Success(it)
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getUserFromServer(email: String): Response<UserEntity> {
        kotlin.runCatching {
            return@runCatching firebaseFirestore.collection(Constants.USERS)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
        }.onSuccess {
            it?.let {
                it.documents[0].toObject(User::class.java)?.also { user ->
                    user.mapObjectTo<User, UserEntity>()?.let { userEntity ->
                        appDatabase.userDao().upsertUser(userEntity)
                        return Response.Success(userEntity)
                    }
                }
            }
        }.onFailure {
            Timber.e(it)
            return Response.Error(it.message.toString())
        }
        return Response.Error(resources.getString(R.string.something_went_wrong))
    }

    override suspend fun getUserFromLocal(id: String): Flow<UserEntity?> {
        return appDatabase.userDao().getUserByIdFlow(id)
    }

    override suspend fun getUserByIdFromLocal(id: String): UserEntity? {
        return appDatabase.userDao().getUserById(id)
    }
}
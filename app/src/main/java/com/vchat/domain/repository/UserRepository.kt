package com.vchat.domain.repository

import android.net.Uri
import com.vchat.common.Response
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.data.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 12/03/23.
 */
interface UserRepository {
    suspend fun login(email: String, password: String): Response<UserEntity>
    suspend fun sendPasswordResetEmail(email: String): Response<Unit>
    suspend fun register(user: User): Response<UserEntity>
    suspend fun createUser(user: User): Response<UserEntity>
    suspend fun getUserFromServer(email: String): Response<UserEntity>
    suspend fun getUserFromServerByUserId(userId: String): Response<UserEntity>
    suspend fun getUserFromLocal(id: String): Flow<UserEntity?>
    suspend fun getUserByIdFromLocal(id: String): UserEntity?
    suspend fun updateUser(user: User): Response<UserEntity>
    suspend fun uploadUserImage(profileUri: Uri, userId: String): Response<String>
    suspend fun deleteAccount(userId: String): Response<Unit>
    suspend fun getUserByEmailFromLocal(email: String): UserEntity?
}
package com.vchat.domain.repository

import com.google.firebase.auth.AuthCredential
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
    suspend fun getUserFromLocal(id: String): Flow<UserEntity?>
}
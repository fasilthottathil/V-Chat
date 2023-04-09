package com.vchat.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vchat.data.local.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fasil on 15/03/23.
 */
@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(userEntity: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserByIdFlow(id: String): Flow<UserEntity>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): UserEntity?

    @Query("DELETE FROM users")
    fun deleteAll()
}
package com.vchat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Fasil on 15/03/23.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    var name: String = "",
    val email: String = "",
    val gender: String = "",
    val profileUrl: String = "",
    var about: String = "",
    val createdOn: Long = System.currentTimeMillis(),
    val isTerminated: Boolean = false,
    val isPremium: Boolean = false
)
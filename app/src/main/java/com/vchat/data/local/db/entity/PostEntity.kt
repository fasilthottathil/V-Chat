package com.vchat.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Fasil on 19/03/23.
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var userId: String = "",
    var profileUrl: String = "",
    var username: String = "",
    var content: String = "",
    var postImageUrl: String = "",
    var postType: String = "",
    var createdOn: Long = System.currentTimeMillis(),
    var isTerminated: Boolean = false
)
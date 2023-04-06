package com.vchat.data.models

import com.vchat.common.enums.PostType

/**
 * Created by Fasil on 19/03/23.
 */
data class Post(
    var id: String = "",
    var userId: String = "",
    var profileUrl: String = "",
    var username: String = "",
    var content: String = "",
    var postImageUrl: String = "",
    var postType: String = PostType.TEXT.name,
    var createdOn: Long = System.currentTimeMillis(),
    var isTerminated: Boolean = false
) {
    fun toMap() = mapOf(
        "id" to id,
        "userId" to userId,
        "profileUrl" to profileUrl,
        "username" to username,
        "content" to content,
        "postImageUrl" to postImageUrl,
        "postType" to postType,
        "createdOn" to createdOn,
        "isTerminated" to isTerminated
    )
}

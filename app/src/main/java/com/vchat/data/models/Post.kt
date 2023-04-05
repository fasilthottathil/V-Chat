package com.vchat.data.models

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
    var postType: String = "",
    var createdOn: Long = System.currentTimeMillis(),
    var isTerminated: Boolean = false
)

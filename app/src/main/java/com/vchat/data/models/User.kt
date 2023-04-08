package com.vchat.data.models

/**
 * Created by Fasil on 12/03/23.
 */
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val gender: String = "",
    var profileUrl: String = "",
    val about: String = "Hey there i'm using V Chat!",
    val createdOn: Long = System.currentTimeMillis(),
    val isTerminated: Boolean = false,
    val isPremium: Boolean = false
) {
    fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "email" to email,
        "password" to password,
        "gender" to gender,
        "profileUrl" to profileUrl,
        "about" to about,
        "createdOn" to createdOn,
        "isTerminated" to isTerminated,
        "isPremium" to isPremium
    )
}

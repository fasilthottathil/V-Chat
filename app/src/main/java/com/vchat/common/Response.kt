package com.vchat.common

/**
 * Created by Fasil on 12/03/23.
 */
sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
}
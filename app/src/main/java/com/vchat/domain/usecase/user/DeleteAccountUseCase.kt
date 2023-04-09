package com.vchat.domain.usecase.user

import com.vchat.data.repository.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
class DeleteAccountUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {
    suspend fun execute(userId: String) = userRepositoryImpl.deleteAccount(userId)
}
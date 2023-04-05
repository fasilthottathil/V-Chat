package com.vchat.domain.usecase.account

import com.vchat.data.models.User
import com.vchat.data.repository.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 18/03/23.
 */
class RegisterUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {
    suspend fun execute(user: User) = userRepositoryImpl.register(user)
}
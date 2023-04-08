package com.vchat.domain.usecase.user

import com.vchat.data.models.User
import com.vchat.data.repository.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 08/04/23.
 */
class UpdateUserUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {
    suspend fun execute(user: User) = userRepositoryImpl.updateUser(user)
}
package com.vchat.domain.usecase.account

import com.vchat.data.repository.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 12/03/23.
 */
class ResetPasswordUseCase @Inject constructor(private val userRepository: UserRepositoryImpl) {
    suspend fun execute(email: String) = userRepository.sendPasswordResetEmail(email)
}
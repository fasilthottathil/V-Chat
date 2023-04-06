package com.vchat.domain.usecase.user

import com.vchat.data.repository.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 26/03/23.
 */
class GetUserByIdFromLocalUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {
    suspend fun execute(id: String) = userRepositoryImpl.getUserByIdFromLocal(id)
}
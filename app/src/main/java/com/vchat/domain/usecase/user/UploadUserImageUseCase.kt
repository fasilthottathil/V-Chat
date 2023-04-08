package com.vchat.domain.usecase.user

import android.net.Uri
import com.vchat.data.repository.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by Fasil on 08/04/23.
 */
class UploadUserImageUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {
    suspend fun execute(profileUri: Uri, userId: String) = userRepositoryImpl.uploadUserImage(profileUri, userId)
}
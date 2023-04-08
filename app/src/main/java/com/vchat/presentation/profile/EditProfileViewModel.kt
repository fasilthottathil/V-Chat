package com.vchat.presentation.profile

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.common.enums.InputType
import com.vchat.data.local.db.entity.UserEntity
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.data.models.User
import com.vchat.domain.usecase.user.GetUserFromLocalUseCase
import com.vchat.domain.usecase.user.UpdateUserUseCase
import com.vchat.domain.usecase.user.UploadUserImageUseCase
import com.vchat.utils.mapObjectTo
import com.vchat.utils.validateInputText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 08/04/23.
 */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val getUserFromLocalUseCase: GetUserFromLocalUseCase,
    private val uploadUserImageUseCase: UploadUserImageUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _user: MutableStateFlow<UserEntity> = MutableStateFlow(UserEntity())
    val user get() = _user.asStateFlow()
    private val _onUpdateUser: MutableState<Boolean> = mutableStateOf(false)
    val onUpdateUser: State<Boolean> get() = _onUpdateUser

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            appPreferenceManager.getMyId()?.let {
                getUserFromLocalUseCase.execute(it).collectLatest { userEntity ->
                    userEntity?.let { _user.emit(userEntity) }
                }
            }
        }
    }

    fun updateUser(userEntity: UserEntity, imageUri: Uri?) {
        val user = userEntity.mapObjectTo<UserEntity, User>()!! //Asserting will not be null
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            if (!user.name.validateInputText(InputType.TEXT).first) {
                stopLoading()
                _error.value = "Invalid username"
                return@launch
            }
            if (imageUri != null) {
                when (val response = uploadUserImageUseCase.execute(imageUri, user.id)) {
                    is Response.Success -> user.profileUrl = response.data
                    is Response.Error -> {
                        stopLoading()
                        _error.value = response.message
                        return@launch
                    }
                }
            }
            when (val response = updateUserUseCase.execute(user)) {
                is Response.Success -> {
                    stopLoading()
                    _onUpdateUser.value = true
                }
                is Response.Error -> {
                    stopLoading()
                    _error.value = response.message
                }
            }
        }
    }

    fun resetError() {
        _error.value = null
    }

    private fun stopLoading() {
        _loading.value = false
    }
}
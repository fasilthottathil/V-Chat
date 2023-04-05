package com.vchat.presentation.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.common.enums.InputType
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.data.models.User
import com.vchat.domain.usecase.account.RegisterUseCase
import com.vchat.utils.validateInputText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * Created by Fasil on 12/03/23.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _onRegistered: MutableState<Boolean> = mutableStateOf(false)
    val onRegistered: State<Boolean> get() = _onRegistered


    fun validateInputFields(username: String, email: String, password: String, gender: String) {
        viewModelScope.launch {
            val validateUsername = username.validateInputText(InputType.TEXT)
            val validateEmail = email.validateInputText(InputType.EMAIL)
            val validatePassword = password.validateInputText(InputType.TEXT_PASSWORD)
            if (!validateUsername.first) {
                _error.value = (validateUsername.second)
                return@launch
            }
            if (!validateEmail.first) {
                _error.value = (validateEmail.second)
                return@launch
            }
            if (!validatePassword.first) {
                _error.value = (validatePassword.second)
                return@launch
            }
            register(
                User(
                    id = UUID.randomUUID().toString(),
                    name = username,
                    email = email,
                    password = password,
                    gender = gender
                )
            )
        }
    }

    private fun register(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            when (val response = registerUseCase.execute(user)) {
                is Response.Success -> {
                    _loading.value = false
                    appPreferenceManager.setLogin(response.data.id, true)
                    _onRegistered.value = true
                    delay(30)
                    _onRegistered.value = false
                }
                is Response.Error -> {
                    _loading.value = false
                    _error.value = response.message
                }
            }
        }
    }

    fun resetError() {
        viewModelScope.launch { _error.value = null }
    }


}
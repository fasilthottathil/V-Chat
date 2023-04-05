package com.vchat.presentation.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.common.enums.InputType
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.domain.usecase.account.LoginUseCase
import com.vchat.utils.validateInputText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 12/03/23.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _onLogin: MutableState<Boolean> = mutableStateOf(false)
    val onLogin: State<Boolean> get() = _onLogin

    fun validateInputFields(email: String, password: String) {
        viewModelScope.launch {
            val validateEmail = email.validateInputText(InputType.EMAIL)
            val validatePassword = password.validateInputText(InputType.TEXT_PASSWORD)
            if (!validateEmail.first) {
                _error.value = (validateEmail.second)
                return@launch
            }
            if (!validatePassword.first) {
                _error.value = (validatePassword.second)
                return@launch
            }
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            when (val response = loginUseCase.execute(email, password)) {
                is Response.Success -> {
                    stopLoading()
                    appPreferenceManager.setLogin(response.data.id, true)
                    _onLogin.value = true
                    delay(30)
                    _onLogin.value = false
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
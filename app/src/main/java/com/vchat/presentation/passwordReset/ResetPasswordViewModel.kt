package com.vchat.presentation.passwordReset

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.common.enums.InputType
import com.vchat.domain.usecase.account.ResetPasswordUseCase
import com.vchat.utils.validateInputText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 14/03/23.
 */
@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _onResetEmailSend: MutableState<Boolean> = mutableStateOf(false)
    val onResetEmailSend: State<Boolean> get() = _onResetEmailSend

    fun validateInputFields(email: String) {
        viewModelScope.launch {
            val validateEmail = email.validateInputText(InputType.EMAIL)
            if (!validateEmail.first) {
                _error.value = (validateEmail.second)
                return@launch
            }
            sendResetPasswordEmail(email)
        }
    }

    private fun sendResetPasswordEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            when (val response = resetPasswordUseCase.execute(email)) {
                is Response.Success -> {
                    stopLoading()
                    _onResetEmailSend.value = true
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
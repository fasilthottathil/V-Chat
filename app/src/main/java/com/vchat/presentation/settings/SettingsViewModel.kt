package com.vchat.presentation.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.common.Response
import com.vchat.data.local.db.AppDatabase
import com.vchat.data.local.pref.AppPreferenceManager
import com.vchat.domain.usecase.user.DeleteAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 09/04/23.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferenceManager: AppPreferenceManager,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val appDatabase: AppDatabase
) : ViewModel() {
    private val _error: MutableState<String?> = mutableStateOf(null)
    val error: State<String?> get() = _error
    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading: State<Boolean> get() = _loading
    private val _onDeleteAccount: MutableState<Boolean> = mutableStateOf(false)
    val onDeleteAccount: State<Boolean> get() = _onDeleteAccount
    private val _onLogout: MutableState<Boolean> = mutableStateOf(false)
    val onLogout: State<Boolean> get() = _onLogout

    fun deleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            appPreferenceManager.getMyId()?.let {
                _loading.value = true
                when (val response = deleteAccountUseCase.execute(it)) {
                    is Response.Success -> {
                        stopLoading()
                        appDatabase.postDao().deleteAll()
                        appDatabase.chatsDao().deleteAll()
                        appDatabase.userDao().deleteAll()
                        appPreferenceManager.logout()
                        _onDeleteAccount.value = true
                    }
                    is Response.Error -> {
                        stopLoading()
                        _error.value = response.message
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.postDao().deleteAll()
            appDatabase.chatsDao().deleteAll()
            appDatabase.userDao().deleteAll()
            appPreferenceManager.logout()
            _onLogout.value = true
        }
    }

    fun resetError() {
        _error.value = null
    }

    private fun stopLoading() {
        _loading.value = false
    }

}
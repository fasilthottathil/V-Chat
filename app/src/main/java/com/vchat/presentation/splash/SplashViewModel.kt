package com.vchat.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vchat.data.local.pref.AppPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Fasil on 11/03/23.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(private val appPreferenceManager: AppPreferenceManager) : ViewModel() {

    fun isLogin() = appPreferenceManager.isLogin()

}
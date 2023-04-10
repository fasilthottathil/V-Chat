package com.vchat.presentation.splash

import androidx.lifecycle.ViewModel
import com.vchat.data.local.pref.AppPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Fasil on 11/03/23.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(private val appPreferenceManager: AppPreferenceManager) : ViewModel() {

    fun isLogin() = appPreferenceManager.isLogin()

}
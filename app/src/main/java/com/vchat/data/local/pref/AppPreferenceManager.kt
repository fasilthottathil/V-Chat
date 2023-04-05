package com.vchat.data.local.pref

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

/**
 * Created by Fasil on 11/03/23.
 */
class AppPreferenceManager @Inject constructor(application: Application) {

    private val sharedPreferences = application.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun isLogin() = sharedPreferences.getBoolean(IS_LOGIN, false)

    fun setLogin(userId: String?, isLogin: Boolean) {
        sharedPreferences.edit {
            putString(USER_ID, userId)
            putBoolean(IS_LOGIN, isLogin)
        }
    }

    fun getMyId() = sharedPreferences.getString(USER_ID, null)

    companion object {
        private const val PREF = "MY_PREF"
        private const val IS_LOGIN = "IS_LOGIN"
        private const val USER_ID = "USER_ID"
    }
}
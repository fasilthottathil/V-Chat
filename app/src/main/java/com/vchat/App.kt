package com.vchat

import android.app.Application
import com.vchat.utils.TimberUtil
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Fasil on 08/03/23.
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TimberUtil.init()
    }
}
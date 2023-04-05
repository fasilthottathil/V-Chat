package com.vchat.utils

import android.util.Log
import com.vchat.BuildConfig
import timber.log.Timber

/**
 * Created by Fasil on 12/03/23.
 */
object TimberUtil {
    private const val TAG = "V Chat"
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            Timber.tag(TAG).e(message)
        }
    }
}
package com.example.codechallengeex.utils

import android.util.Log
import com.example.codechallengeex.BuildConfig

/**
 * Created by Satya Seshu on 10/04/22.
 */
object LoggerInfo {

    fun printLog(from: String?, msg: Any?) {
        if (BuildConfig.DEBUG) {
            Log.d("code_challenge", "$from : $msg")
        }
    }

    fun errorLog(from: String?, msg: Any?) {
        if (BuildConfig.DEBUG) {
            Log.e("code_challenge", "$from : $msg")
        }
    }
}
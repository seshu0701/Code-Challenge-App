package com.example.codechallengeex

import android.app.Application
import android.content.Context
import com.example.codechallengeex.data.ApiHelper
import com.example.codechallengeex.data.RetrofitModule
import com.example.codechallengeex.data.interfaces.ApiInterface

/**
 * Created by Satya Seshu on 10/04/22.
 */
class MyApplication : Application() {

    companion object {

        private var mApiHelper: ApiHelper? = null
        fun getApiHelperInstance(context: Context): ApiHelper {
            if (mApiHelper == null) {
                val retrofitModule = RetrofitModule.getInstance(context)
                mApiHelper =
                    retrofitModule.client?.create(ApiInterface::class.java)?.let { ApiHelper(it) }
            }
            return mApiHelper!!
        }
    }
}
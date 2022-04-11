package com.example.codechallengeex.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codechallengeex.data.network.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

/**
 * Created by Satya Seshu on 10/04/22.
 */
open class BaseViewModel : ViewModel() {

    private val viewModelJob = Job()
    val networkState = MutableLiveData(NetworkState.LOADED)
    private val messageLiveData = MutableLiveData<String>()
    val errorMessageLiveData = MutableLiveData<String>()

    val uiScope = CoroutineScope(Dispatchers.Main)

    /**
     * This is a scope for all coroutines launched by [BaseViewModel]
     * that will be dispatched in a Pool of Thread
     */
    val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancel()
        ioScope.coroutineContext.cancel()
        viewModelJob.cancel()
    }

    /**
     * Network connection live data
     */
    fun networkLiveData(context: Context) = ConnectionLiveData(context)

    /**
     * Show response message by ViewModel
     */
    fun getMessageData() = messageLiveData

    /**
     * Show response error message by ViewModel
     */
    fun getErrorMessageData() = errorMessageLiveData

    /**
     * Show message
     * @param message
     */
    fun showMessage(message: String) {
        messageLiveData.value = message
    }

    /**
     * Show error message
     * @param message
     */
    fun showErrorMessage(message: String) {
        errorMessageLiveData.value = message
    }
}
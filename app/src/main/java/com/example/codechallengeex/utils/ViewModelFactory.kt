package com.example.codechallengeex.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.codechallengeex.data.ApiHelper
import com.example.codechallengeex.ui.MainRepository
import com.example.codechallengeex.ui.MainViewModel

/**
 * Created by Satya Seshu on 10/04/22.
 */
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Class not found")
    }
}
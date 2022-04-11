package com.example.codechallengeex.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.codechallengeex.base.BaseViewModel
import com.example.codechallengeex.data.network.NetworkState
import com.example.codechallengeex.model.ResponseData
import com.example.codechallengeex.utils.Constants
import kotlinx.coroutines.launch

/**
 * Created by Satya Seshu on 10/04/22.
 */
class MainViewModel(private val mainRepository: MainRepository) : BaseViewModel() {

    private val _itemsListByDate = MutableLiveData<MutableList<ResponseData>>()
    val itemsListDate: LiveData<MutableList<ResponseData>>
        get() = _itemsListByDate

    fun getAllImagesByDate(startDate: String?, endDate: String?) = viewModelScope.launch {
        networkState.postValue(NetworkState.LOADING)
        val paramsMap: MutableMap<String, String?> = HashMap()
        paramsMap["start_date"] = startDate
        paramsMap["end_date"] = endDate
        paramsMap["api_key"] = Constants.API_KEY
        try {
            val responseResult = mainRepository.getAllImagesByDate(paramsMap)
            networkState.postValue(NetworkState.LOADED)
            if (responseResult.isSuccessful) {
                _itemsListByDate.value = responseResult.body()
            } else {
                errorMessageLiveData.value = responseResult.message()
            }
        } catch (ex: Exception) {
            errorMessageLiveData.value = ex.message
        }
    }

    fun getAllImagesByCurrentDate(startDate: String?) = viewModelScope.launch {
        networkState.postValue(NetworkState.LOADING)
        val paramsMap: MutableMap<String, String?> = HashMap()
        paramsMap["date"] = startDate
        paramsMap["api_key"] = Constants.API_KEY
        try {
            val responseResult = mainRepository.getAllImagesByCurrentDate(paramsMap)
            networkState.postValue(NetworkState.LOADED)
            if (responseResult.isSuccessful) {
                val responseData = responseResult.body()
                if (responseData != null) {
                    val list: MutableList<ResponseData> = ArrayList()
                    list.add(responseData)
                    _itemsListByDate.value = list
                }
            } else {
                errorMessageLiveData.value = responseResult.message()
            }
        } catch (ex: Exception) {
            errorMessageLiveData.value = ex.message
        }
    }
}
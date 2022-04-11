package com.example.codechallengeex.ui

import com.example.codechallengeex.data.ApiHelper

/**
 * Created by Satya Seshu on 10/04/22.
 */
class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getAllImagesByDate(paramsMap: MutableMap<String, String?>) =
        apiHelper.getAllImagesByDate(paramsMap)

    suspend fun getAllImagesByCurrentDate(paramsMap: MutableMap<String, String?>) =
        apiHelper.getAllImagesByCurrentDate(paramsMap)
}
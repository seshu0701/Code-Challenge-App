package com.example.codechallengeex.data

import com.example.codechallengeex.data.interfaces.ApiInterface

/**
 * Created by Satya Seshu on 10/04/22.
 */
class ApiHelper(private val apiInterface: ApiInterface) {

    suspend fun getAllImagesByDate(paramsMap: MutableMap<String, String?>) =
        apiInterface.getAllImagesByDate(paramsMap)

    suspend fun getAllImagesByCurrentDate(paramsMap: MutableMap<String, String?>) =
        apiInterface.getAllImagesByCurrentDate(paramsMap)
}
package com.example.codechallengeex.data.interfaces

import com.example.codechallengeex.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by Satya Seshu on 10/04/22.
 */
interface ApiInterface {

    @GET("apod")
    suspend fun getAllImagesByDate(
        @QueryMap paramsMap: MutableMap<String, String?>
    ): Response<MutableList<ResponseData>>

    @GET("apod")
    suspend fun getAllImagesByCurrentDate(
        @QueryMap paramsMap: MutableMap<String, String?>
    ): Response<ResponseData>
}
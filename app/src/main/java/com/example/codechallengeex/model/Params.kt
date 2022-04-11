package com.example.codechallengeex.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Satya Seshu on 09/04/22.
 */

data class ContentModel(var status: String?, var value: Any?)

data class ResponseData(
    @SerializedName("copyright") var copyright: String?,
    @SerializedName("date") var date: String?,
    @SerializedName("explanation") var explanation: String?,
    @SerializedName("hdurl") var hdurl: String?,
    @SerializedName("media_type") var mediaType: String?,
    @SerializedName("service_version") var service_version: String?,
    @SerializedName("title") var title: String?,
    @SerializedName("url") var url: String?,
    var isFavourites: Boolean
)

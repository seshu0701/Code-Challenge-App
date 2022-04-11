package com.example.codechallengeex

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.example.codechallengeex.utils.UnsafeOkHttpClient
import java.io.InputStream

/**
 * Created by Satya Seshu on 10/04/22.
 */
@GlideModule
class GlideAppApplication : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        val factory: OkHttpUrlLoader.Factory = OkHttpUrlLoader.Factory(client)
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
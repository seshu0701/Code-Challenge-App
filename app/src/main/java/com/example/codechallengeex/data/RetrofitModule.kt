package com.example.codechallengeex.data

import android.content.Context
import com.example.codechallengeex.utils.Constants
import com.example.codechallengeex.utils.NetworkUtils
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Created by Satya Seshu on 10/04/22.
 */
class RetrofitModule(private val context: Context) {

    companion object {
        fun getInstance(context: Context): RetrofitModule {
            return RetrofitModule(context)
        }
    }

    private val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpCacheDirectory = File(context.cacheDir, "responses")
    private val cacheSize = 20 * 1024 * 1024
    private val cache = Cache(httpCacheDirectory, cacheSize.toLong())
    private var retrofit: Retrofit? = null
    private lateinit var okHttpClient: OkHttpClient

    init {
        init()
    }

    private fun init() {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor) //.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(Interceptor { chain ->
                var request = chain.request()
                if (!NetworkUtils.isNetworkConnected(context)) {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
                chain.proceed(request)
            })
            .addNetworkInterceptor(Interceptor { chain ->
                val response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(30, TimeUnit.SECONDS)
                    .build()
                response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            })
            .cache(provideOkHttpCache())
            .build()
    }


    var gson = GsonBuilder()
        .setLenient()
        .create()


    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }

    private val offlineCacheController = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        if (NetworkUtils.isNetworkConnected(context)) {
            val maxAge = 60 // read from cache for 1 minute
            originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
    }

    private var OFFLINE_INTERCEPTOR = Interceptor { chain ->
        var request = chain.request()
        if (!NetworkUtils.isNetworkConnected(context)) {
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
        chain.proceed(request)
    }


    private var ONLINE_INTERCEPTOR = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 // read from cache
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .build()
    }

    private fun provideOkHttpCache(): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(context.cacheDir, cacheSize.toLong())
    }
}
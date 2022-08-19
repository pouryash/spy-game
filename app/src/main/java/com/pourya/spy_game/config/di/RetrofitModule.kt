package com.pourya.spy_game.config.di


import android.content.Context
import com.pourya.spy_game.service.Api
import com.pourya.spy_game.util.Common
import com.pourya.spy_game.util.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val HEADER_CACHE_CONTROL = "Cache-Control"
const val HEADER_PRAGMA = "Pragma"


val retrofitModule = module {

    single {
        okHttpClint(androidContext())
    }

    single {

        retrofit(Constants.BASE_URL, get())
    }

    single {
        get<Retrofit>().create(Api::class.java)
    }

}

private fun offlineInterceptor(context: Context): Interceptor {
    return Interceptor { chain ->
        var request = chain.request()

        // prevent caching when network is on. For that we use the "networkInterceptor"
        if (!Common.isNetworkConnected(context)) {
            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()
            request = request.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl)
                .build()
        }
        chain.proceed(request)
    }
}


private fun networkInterceptor(): Interceptor {
    return Interceptor { chain ->
        val response: Response = chain.proceed(chain.request())

        response.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .build()
    }
}


private fun okHttpClint(context: Context):OkHttpClient {

    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(context.cacheDir, cacheSize)


    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


    return OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .cache(myCache)
        .addInterceptor(loggingInterceptor)
        .addNetworkInterceptor(networkInterceptor())
        .addInterceptor(offlineInterceptor(context))
        .build()

}

private fun retrofit(baseUrl: String, okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

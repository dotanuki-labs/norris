package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.networking.RetrofitBuilder
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.wireRestApi(): ChuckNorrisService {
    val url = url("/").toString()

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    return RetrofitBuilder(url.toHttpUrl(), client).create(ChuckNorrisService::class.java)
}

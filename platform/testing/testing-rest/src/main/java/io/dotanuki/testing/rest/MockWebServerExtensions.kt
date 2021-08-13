package io.dotanuki.testing.rest

import io.dotanuki.norris.networking.RetrofitBuilder
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.wireRestApi(): ChuckNorrisDotIO {
    val url = url("/").toString()

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    return RetrofitBuilder(url.toHttpUrl(), client).create(ChuckNorrisDotIO::class.java)
}

package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.networking.RetrofitBuilder
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient

object ChuckNorrisApi {

    private val httpClient by lazy {
        OkHttpClient.Builder().build()
    }

    operator fun invoke(url: String): ChuckNorrisService =
        RetrofitBuilder.invoke(url.toHttpUrl(), httpClient).create(ChuckNorrisService::class.java)
}

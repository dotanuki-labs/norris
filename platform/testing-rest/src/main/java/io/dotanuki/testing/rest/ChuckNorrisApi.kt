package io.dotanuki.testing.rest

import io.dotanuki.norris.networking.RetrofitBuilder
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient

object ChuckNorrisApi {

    private val httpClient by lazy {
        OkHttpClient.Builder().build()
    }

    operator fun invoke(url: String): ChuckNorrisDotIO =
        RetrofitBuilder.invoke(url.toHttpUrl(), httpClient).create(ChuckNorrisDotIO::class.java)
}

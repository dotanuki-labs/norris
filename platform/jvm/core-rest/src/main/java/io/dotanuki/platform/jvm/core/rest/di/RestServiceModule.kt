package io.dotanuki.platform.jvm.core.rest.di

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.dotanuki.platform.jvm.core.rest.RetrofitBuilder
import okhttp3.HttpUrl

class RestServiceModule(private val apiUrl: HttpUrl) {

    val chuckNorrisServiceClient by lazy {
        val resilience = HttpResilience.createDefault()
        val retrofit = RetrofitBuilder(apiUrl, resilience)
        val service = retrofit.create(ChuckNorrisService::class.java)
        ChuckNorrisServiceClient(service, resilience)
    }
}

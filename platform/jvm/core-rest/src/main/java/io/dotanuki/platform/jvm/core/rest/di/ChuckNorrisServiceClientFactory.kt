package io.dotanuki.platform.jvm.core.rest.di

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.dotanuki.platform.jvm.core.rest.RetrofitBuilder
import okhttp3.HttpUrl

object ChuckNorrisServiceClientFactory {

    private var memoized: ChuckNorrisServiceClient? = null

    context (ApiUrlFactory)
    fun create(): ChuckNorrisServiceClient =
        memoized ?: newClient(apiUrl).apply { memoized = this }

    private fun newClient(apiUrl: HttpUrl): ChuckNorrisServiceClient {
        val resilience = HttpResilience.createDefault()
        val retrofit = RetrofitBuilder(apiUrl, resilience)
        val service = retrofit.create(ChuckNorrisService::class.java)
        return ChuckNorrisServiceClient(service, resilience)
    }
}

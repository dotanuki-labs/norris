package io.dotanuki.platform.jvm.core.rest.di

import io.dotanuki.platform.jvm.core.rest.RetrofitBuilder
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val restServiceModule = DI.Module("rest-service") {

    bind {
        singleton {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttp = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val retrofit = RetrofitBuilder(
                apiURL = instance(),
                httpClient = okHttp
            )

            val service = retrofit.create(ChuckNorrisService::class.java)
            ChuckNorrisServiceClient(service)
        }
    }
}

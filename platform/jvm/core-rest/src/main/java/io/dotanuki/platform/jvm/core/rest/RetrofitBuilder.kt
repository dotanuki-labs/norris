package io.dotanuki.platform.jvm.core.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit

object RetrofitBuilder {

    private val jsonConfig by lazy {
        Json {
            isLenient = false
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
        }
    }

    private val contentType by lazy {
        "application/json".toMediaTypeOrNull()!!
    }

    private fun createHttpClient(config: HttpResilience): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .callTimeout(config.timeoutForHttpRequest)
            .build()
    }

    operator fun invoke(apiURL: HttpUrl, config: HttpResilience): Retrofit =
        with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(createHttpClient(config))
            addConverterFactory(jsonConfig.asConverterFactory(contentType))
            build()
        }
}

package io.dotanuki.platform.jvm.core.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit

object ChuckNorrisServiceBuilder {

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

    fun build(apiURL: String, config: HttpResilience): ChuckNorrisService {
        val retroft = with(Retrofit.Builder()) {
            baseUrl(apiURL.toHttpUrl())
            client(createHttpClient(config))
            addConverterFactory(jsonConfig.asConverterFactory(contentType))
            build()
        }

        return retroft.create(ChuckNorrisService::class.java)
    }
}

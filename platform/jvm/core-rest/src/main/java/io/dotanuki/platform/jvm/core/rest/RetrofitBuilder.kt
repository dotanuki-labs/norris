package io.dotanuki.platform.jvm.core.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.dotanuki.platform.jvm.core.rest.internal.ResilienceConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
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

    private val standardHttpClient by lazy {
        val logger = HttpLoggingInterceptor().setLevel(Level.BODY)

        OkHttpClient.Builder()
            .addInterceptor(logger)
            .callTimeout(ResilienceConfiguration.HTTP_REQUEST_TIMEOUT)
            .build()
    }

    operator fun invoke(apiURL: HttpUrl, httpClient: OkHttpClient = standardHttpClient): Retrofit =
        with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(httpClient)
            addConverterFactory(jsonConfig.asConverterFactory(contentType))
            build()
        }
}

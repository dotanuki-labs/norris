package io.dotanuki.norris.networking

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@ExperimentalSerializationApi
object RetrofitBuilder {

    operator fun invoke(apiURL: HttpUrl, httpClient: OkHttpClient) =
        with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(httpClient)
            addConverterFactory(jsonConfig.asConverterFactory(contentType))
            build()
        }

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
}
package io.dotanuki.app.di

import io.dotanuki.app.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

object ApplicationModule {

    val apiUrl by lazy {
        val url = when {
            BuildConfig.IS_TEST_MODE -> "https://norris.wiremockapi.cloud/"
            else -> "https://api.chucknorris.io"
        }
        requireNotNull(url.toHttpUrlOrNull())
    }
}

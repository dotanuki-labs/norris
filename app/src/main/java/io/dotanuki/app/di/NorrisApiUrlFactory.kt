package io.dotanuki.app.di

import io.dotanuki.app.BuildConfig
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory

object NorrisApiUrlFactory : ApiUrlFactory {

    override val apiUrl by lazy {
        when {
            BuildConfig.IS_TEST_MODE -> "https://norris.wiremockapi.cloud/"
            else -> "https://api.chucknorris.io"
        }
    }
}

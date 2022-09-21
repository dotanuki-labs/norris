package io.dotanuki.demos.norris.di

import io.dotanuki.demos.norris.navigation.ScreenLinks
import io.dotanuki.norris.app.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val applicationModule = DI.Module("application") {

    bind {
        singleton {
            val url = when {
                BuildConfig.IS_TEST_MODE -> "https://norris-app.mocklab.io/"
                else -> "https://api.chucknorris.io"
            }
            requireNotNull(url.toHttpUrlOrNull())
        }
    }

    bind {
        singleton {
            ScreenLinks.associations
        }
    }
}

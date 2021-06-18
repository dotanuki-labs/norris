package io.dotanuki.demos.norris.di

import io.dotanuki.demos.norris.BuildConfig
import io.dotanuki.demos.norris.logger.LogcatLogger
import io.dotanuki.demos.norris.navigation.ScreenLinks
import kotlinx.coroutines.Dispatchers
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val applicationModule = DI.Module("application") {

    bind {
        singleton {
            requireNotNull(
                BuildConfig.CHUCKNORRIS_API_URL.toHttpUrlOrNull()
            )
        }
    }

    bind {
        singleton {
            LogcatLogger
        }
    }

    bind {
        singleton {
            ScreenLinks.associations
        }
    }

    bind {
        singleton {
            Dispatchers.IO
        }
    }
}

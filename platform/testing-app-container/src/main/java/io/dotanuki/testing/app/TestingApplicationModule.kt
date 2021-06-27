package io.dotanuki.testing.app

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val containerApplicationModule = DI.Module("application") {

    bind {
        singleton {
            requireNotNull(
                "http://localhost:4242".toHttpUrlOrNull()
            )
        }
    }

    bind {
        singleton {
            LogcatLogger
        }
    }
}

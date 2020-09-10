package io.dotanuki.demos.norris.di

import io.dotanuki.demos.norris.logger.LogcatLogger
import io.dotanuki.demos.norris.navigation.ScreenLinks
import io.dotanuki.logger.Logger
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val applicationModule = DI.Module("application") {

    bind<Logger>() with singleton {
        LogcatLogger
    }

    bind() from singleton {
        ScreenLinks.associations
    }

    bind() from singleton {
        Dispatchers.IO
    }
}
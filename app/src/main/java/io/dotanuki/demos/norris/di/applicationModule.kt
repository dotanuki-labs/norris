package io.dotanuki.demos.norris.di

import io.dotanuki.demos.norris.logger.LogcatLogger
import io.dotanuki.demos.norris.navigation.ScreenLinks
import io.dotanuki.logger.Logger
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val applicationModule = Kodein.Module("application") {

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
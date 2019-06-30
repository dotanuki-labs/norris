package io.dotanuki.demos.norris.di

import io.dotanuki.demos.norris.logger.LogcatLogger
import io.dotanuki.demos.norris.navigation.ScreenLinks
import io.dotanuki.logger.Logger
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.features.utilties.KodeinTags
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val applicationModule = Kodein.Module("application") {

    bind<Logger>() with singleton {
        LogcatLogger
    }

    bind() from singleton {
        ScreenLinks.associations
    }

    bind() from provider {
        Navigator(
            host = instance(KodeinTags.hostActivity),
            links = instance()
        )
    }
}


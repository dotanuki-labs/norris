package io.dotanuki.norris.navigator.di

import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.navigator.Navigator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val navigatorModule = DI.Module("navigator") {
    bind() from provider {
        Navigator(
            host = instance(KodeinTags.hostActivity),
            links = instance()
        )
    }
}
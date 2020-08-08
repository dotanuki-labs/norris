package io.dotanuki.norris.navigator.di

import io.dotanuki.norris.features.utilties.KodeinTags
import io.dotanuki.norris.navigator.Navigator
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val navigatorModule = Kodein.Module("navigator") {
    bind() from provider {
        Navigator(
            host = instance(KodeinTags.hostActivity),
            links = instance()
        )
    }
}
package io.dotanuki.platform.android.core.navigator.di

import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.jvm.core.kodein.KodeinTags
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val navigatorModule = DI.Module("navigator") {
    bind {
        provider {
            Navigator(
                host = instance(KodeinTags.hostActivity),
                links = instance()
            )
        }
    }
}

package io.dotanuki.app

import android.app.Application
import io.dotanuki.app.di.applicationModule
import io.dotanuki.features.facts.di.factsModule
import io.dotanuki.features.search.di.searchModule
import io.dotanuki.platform.android.core.navigator.di.navigatorModule
import io.dotanuki.platform.android.core.persistance.di.persistanceModule
import io.dotanuki.platform.jvm.core.rest.di.restServiceModule
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

class DependenciesSetup(private val app: Application) {

    val container by lazy {
        DI {
            modules.forEach { import(it) }
            bind {
                singleton { app }
            }
        }
    }

    private val modules = listOf(
        applicationModule,
        restServiceModule,
        persistanceModule,
        navigatorModule,
        factsModule,
        searchModule
    )
}

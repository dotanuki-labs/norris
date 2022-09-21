package io.dotanuki.demos.norris

import android.app.Application
import io.dotanuki.demos.norris.di.applicationModule
import io.dotanuki.norris.features.facts.di.factsModule
import io.dotanuki.norris.features.search.di.searchModule
import io.dotanuki.norris.navigator.di.navigatorModule
import io.dotanuki.norris.persistance.di.persistanceModule
import io.dotanuki.platform.jvm.core.rest.di.restInfrastructureModule
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
        restInfrastructureModule,
        persistanceModule,
        navigatorModule,
        factsModule,
        searchModule
    )
}

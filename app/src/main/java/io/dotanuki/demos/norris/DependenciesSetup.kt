package io.dotanuki.demos.norris

import android.app.Application
import io.dotanuki.demos.norris.di.applicationModule
import io.dotanuki.norris.facts.di.factsModule
import io.dotanuki.norris.navigator.di.navigatorModule
import io.dotanuki.norris.onboarding.di.onboardingModule
import io.dotanuki.norris.persistance.di.persistanceModule
import io.dotanuki.norris.rest.di.restInfrastructureModule
import io.dotanuki.norris.search.di.searchModule
import org.kodein.di.bind
import org.kodein.di.conf.ConfigurableDI
import org.kodein.di.singleton

class DependenciesSetup(private val app: Application) {

    val container by lazy {
        ConfigurableDI(mutable = true).apply {
            modules.forEach { addImport(it) }

            addConfig {
                bind() from singleton { app }
            }
        }
    }

    private val modules = listOf(
        applicationModule,
        restInfrastructureModule,
        persistanceModule,
        navigatorModule,
        onboardingModule,
        factsModule,
        searchModule
    )
}
package io.dotanuki.testing.app

import android.app.Application
import io.dotanuki.norris.navigator.di.navigatorModule
import io.dotanuki.norris.persistance.di.persistanceModule
import io.dotanuki.norris.rest.di.restInfrastructureModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton

class ContainerApplication : Application(), DIAware {

    private val container by lazy {
        DI {
            modules.forEach { import(it) }
            bind <Application>{
                singleton { this@ContainerApplication }
            }
        }
    }

    var modules = mutableListOf(
        containerApplicationModule,
        restInfrastructureModule,
        persistanceModule,
        navigatorModule
    )

    override val di by lazy { container }
}

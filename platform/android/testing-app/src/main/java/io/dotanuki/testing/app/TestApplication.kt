package io.dotanuki.testing.app

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.norris.navigator.di.navigatorModule
import io.dotanuki.norris.persistance.di.persistanceModule
import io.dotanuki.norris.rest.di.restInfrastructureModule
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton

class TestApplication : Application(), DIAware {

    private val container by lazy {
        DI {
            modules.forEach { import(it, allowOverride = true) }
            bind<Application> {
                singleton { this@TestApplication }
            }
        }
    }

    private val containerApplicationModule = DI.Module("application") {
        bind {
            singleton {
                requireNotNull(
                    "http://localhost".toHttpUrlOrNull()
                )
            }
        }
    }

    private var modules = mutableListOf(
        containerApplicationModule,
        restInfrastructureModule,
        persistanceModule,
        navigatorModule
    )

    override val di by lazy { container }

    companion object {
        fun setupWith(vararg extrasModules: DI.Module) {

            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val app = instrumentation.targetContext.applicationContext as TestApplication

            app.run {
                extrasModules.forEach {
                    modules += it
                }
            }
        }
    }
}

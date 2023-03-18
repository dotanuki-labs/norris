package io.dotanuki.platform.android.testing.app

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.platform.android.core.navigator.di.navigatorModule
import io.dotanuki.platform.android.core.persistance.di.persistanceModule
import io.dotanuki.platform.jvm.core.rest.di.restServiceModule
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
        restServiceModule,
        persistanceModule,
        navigatorModule
    )

    override val di by lazy { container }

    companion object {
        fun setupWith(vararg extrasModules: DI.Module): TestApplication {

            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val app = instrumentation.targetContext.applicationContext as TestApplication

            return app.apply {
                extrasModules.forEach {
                    modules += it
                }
            }
        }
    }
}

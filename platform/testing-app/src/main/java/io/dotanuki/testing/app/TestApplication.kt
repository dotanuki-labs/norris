package io.dotanuki.testing.app

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.logger.ConsoleLogger
import io.dotanuki.norris.navigator.di.navigatorModule
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.persistance.di.persistanceModule
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.testing.rest.FakeChuckNorrisIO
import io.dotanuki.testing.rest.testRestInfrastructureModule
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.singleton

class TestApplication : Application(), DIAware {

    private val container by lazy {
        DI {
            modules.forEach { import(it) }
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

        bind {
            singleton {
                ConsoleLogger
            }
        }
    }

    var modules = mutableListOf(
        containerApplicationModule,
        testRestInfrastructureModule,
        persistanceModule,
        navigatorModule
    )

    lateinit var localStorage: LocalStorage
    lateinit var api: FakeChuckNorrisIO

    override val di by lazy { container }

    companion object {
        fun setupWith(module: DI.Module): TestApplication {

            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val app = instrumentation.targetContext.applicationContext as TestApplication

            return app.apply {
                modules += module
                localStorage = di.direct.instance()
                api = di.direct.instance<ChuckNorrisDotIO>() as FakeChuckNorrisIO
            }
        }
    }
}

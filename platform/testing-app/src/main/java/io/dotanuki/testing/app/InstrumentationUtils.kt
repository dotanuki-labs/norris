package io.dotanuki.testing.app

import androidx.test.platform.app.InstrumentationRegistry
import org.kodein.di.DI

fun setupContainerApp(module: DI.Module): TestApplication {

    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val app = instrumentation.targetContext.applicationContext as TestApplication

    return app.apply {
        modules += module
    }
}

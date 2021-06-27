package io.dotanuki.testing.app

import androidx.test.platform.app.InstrumentationRegistry
import org.kodein.di.DI

fun setupContainerApp(module: DI.Module): ContainerApplication {

    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val app = instrumentation.targetContext.applicationContext as ContainerApplication

    return app.apply {
        modules += module
    }
}

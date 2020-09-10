package io.dotanuki.demos.norris.util

import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.demos.norris.NorrisApplication
import org.junit.rules.ExternalResource
import org.kodein.di.DI

class BindingsOverrider(private val bindings: DI.MainBuilder.() -> Unit) : ExternalResource() {

    init {
        val container = app().di
        container.addConfig { bindings() }
    }

    private fun app() =
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as NorrisApplication
}
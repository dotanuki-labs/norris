package io.dotanuki.demos.norris.util

import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.demos.norris.NorrisApplication
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class BindingsOverrider(private val bindings: Kodein.MainBuilder.() -> Unit) : ExternalResource() {

    init {
        val container = app().kodein
        container.addConfig { bindings() }
    }

    private fun app() =
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as NorrisApplication
}
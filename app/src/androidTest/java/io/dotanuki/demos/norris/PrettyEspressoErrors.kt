package io.dotanuki.demos.norris

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import radiography.Radiography

object PrettyEspressoErrors {

    fun install() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val installedFailureHandler = DefaultFailureHandler(context)

        Espresso.setFailureHandler { error, viewMatcher ->
            try {
                installedFailureHandler.handle(error, viewMatcher)
            } catch (incoming: Throwable) {
                redecorate(incoming)
            }
        }
    }

    private fun redecorate(incoming: Throwable): Nothing {
        val detailMessageField = Throwable::class.java.getDeclaredField("detailMessage")
        val previousAccessible = detailMessageField.isAccessible

        try {
            detailMessageField.isAccessible = true
            var actualMessage = (detailMessageField[incoming] as String?).orEmpty()
            actualMessage = actualMessage.substringBefore("\nView Hierarchy:")
            actualMessage += "\nView hierarchies:\n${Radiography.scan()}"
            detailMessageField[incoming] = actualMessage
        } finally {
            detailMessageField.isAccessible = previousAccessible
        }
        throw incoming
    }
}

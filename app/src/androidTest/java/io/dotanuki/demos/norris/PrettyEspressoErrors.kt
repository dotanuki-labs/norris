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
                redecorate(incoming, viewMatcher)
            }
        }
    }

    private fun redecorate(incoming: Throwable, viewMatcher: Matcher<View>): Nothing {
        val detailMessageField = Throwable::class.java.getDeclaredField("detailMessage")
        val previousAccessible = detailMessageField.isAccessible

        try {
            detailMessageField.isAccessible = true
            detailMessageField[incoming] = Radiography.scan()
        } finally {
            detailMessageField.isAccessible = previousAccessible
        }
        throw incoming
    }
}

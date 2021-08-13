package io.dotanuki.testing.screenshots

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity

inline fun <reified T : Activity> prepareToCaptureScreenshot(
    crossinline beforeScreenshot: (T) -> Unit
): T =
    launchActivity<T>().let { scenario ->
        var target: T? = null

        scenario.run {
            moveToState(Lifecycle.State.RESUMED)
            onActivity {
                beforeScreenshot(it)
                target = it
            }
        }

        target ?: throw IllegalStateException("Error when setting up Activity Scenario")
    }

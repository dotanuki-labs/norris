package io.dotanuki.testing.screenshots

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario

inline fun <reified T : Activity> ActivityScenario<T>.screenshot(
    crossinline prepare: (T) -> Unit,
    crossinline capture: (T) -> Unit
) {
    moveToState(Lifecycle.State.CREATED)

    onActivity {
        prepare(it)
        Thread.sleep(2000L)
    }

    moveToState(Lifecycle.State.RESUMED)
    onActivity { capture(it) }
}

package io.dotanuki.platform.android.testing.app

import android.app.Activity
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import org.robolectric.Shadows

inline fun <reified T : Activity> whenActivityResumed(noinline verification: (T) -> Unit) =
    launchActivity<T>().let {
        ActivityScenarioLauncher(it, verification).execute()
        it.close()
    }

class ActivityScenarioLauncher<T : Activity>(
    private val target: ActivityScenario<T>,
    private val verification: (T) -> Unit
) {

    fun execute() {
        target.moveToState(Lifecycle.State.RESUMED)
        awaitPendingExecutions()
        target.onActivity { verification(it) }
    }
}

fun awaitPendingExecutions() {
    Thread.sleep(500L)
    Shadows.shadowOf(Looper.getMainLooper()).idle()
}

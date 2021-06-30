package io.dotanuki.testing.app

import android.app.Activity
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import org.robolectric.Shadows

inline fun <reified T : Activity> activityScenario(target: ActivityScenarioLauncher<T>.() -> Unit) =
    launchActivity<T>().let {
        ActivityScenarioLauncher(it).apply(target)
        it.close()
    }

class ActivityScenarioLauncher<T : Activity>(private val target: ActivityScenario<T>) {

    fun afterLaunch(beforeResume: () -> Any) {
        beforeResume()
        target.moveToState(Lifecycle.State.RESUMED)
    }

    fun whenResumed(whenResumed: (T) -> Unit) {
        target.onActivity { whenResumed(it) }
    }
}

fun ActivityScenarioLauncher<*>.awaitPendingExecutions() {
    Thread.sleep(500)
    Shadows.shadowOf(Looper.getMainLooper()).idle()
}

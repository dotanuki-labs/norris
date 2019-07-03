package io.dotanuki.demos.norris.util

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity

class ActivityScenarioLauncher<T : Activity>(private val scenario: ActivityScenario<T>) {

    private lateinit var beforeLaunch: () -> Any

    fun beforeLaunch(block: () -> Any) {
        beforeLaunch = block
    }

    fun onResume(afterLaunch: (ActivityScenario<*>) -> Any) {
        beforeLaunch.invoke()
        scenario.moveToState(Lifecycle.State.RESUMED)
        afterLaunch.invoke(scenario)
        scenario.close()
    }

    companion object {
        inline fun <reified T : Activity> scenarioLauncher() =
            ActivityScenarioLauncher(launchActivity<T>())
    }
}
package io.dotanuki.platform.android.testing.screenshots

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dropbox.dropshots.Dropshots
import com.dropbox.dropshots.ThresholdValidator
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ScreenshotMatcher<A : Activity, State>(
    private val targetClass: Class<A>,
    private val driver: ScreenshotDriver<A, State>
) : TestRule {

    private val activityRule by lazy {
        ActivityScenarioRule(targetClass)
    }

    private val dropshots by lazy {
        Dropshots(resultValidator = ThresholdValidator(0.1f))
    }

    private val chain by lazy {
        RuleChain.outerRule(activityRule).around(dropshots)
    }

    override fun apply(base: Statement, description: Description): Statement = chain.apply(base, description)

    fun match(state: State) {
        activityRule.scenario.run {

            moveToState(Lifecycle.State.CREATED)

            onActivity { target ->
                driver.beforeCapturing(target, state)
                Thread.sleep(1000L)
            }

            moveToState(Lifecycle.State.RESUMED)

            onActivity { target ->
                dropshots.assertSnapshot(
                    activity = target,
                    name = driver.imageName(state)
                )
            }
        }
    }

    companion object {
        inline fun <reified A : Activity, State> create(driver: ScreenshotDriver<A, State>) =
            ScreenshotMatcher(A::class.java, driver)
    }
}

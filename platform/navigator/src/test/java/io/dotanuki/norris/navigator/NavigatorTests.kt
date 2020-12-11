package io.dotanuki.norris.navigator

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.dotanuki.norris.navigator.Screen.FactsList
import io.dotanuki.norris.navigator.Screen.SearchQuery
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class NavigatorTests {

    class LauncherActivity : FragmentActivity()
    class DestinationActivity : Activity()

    private lateinit var navigator: Navigator
    private lateinit var launcher: LauncherActivity

    private val links = mapOf<Screen, Class<out Activity>>(
        SearchQuery to DestinationActivity::class.java
    )

    @Before fun `before each test`() {
        val origin = LauncherActivity::class.java
        launcher = Robolectric.buildActivity(origin).create(Bundle.EMPTY).get()
        navigator = Navigator(launcher, links)
    }

    @Test fun `should navigate to supported screen`() {
        val shadowActivity = shadowOf(launcher)

        navigator.navigateTo(SearchQuery)

        val launched = shadowActivity.nextStartedActivity
        assertThat(launched.component?.shortClassName).isEqualTo(DestinationActivity::class.java.name)
    }

    @Test fun `should throw when navigating to unsupported screen`() {

        assertThatThrownBy { navigator.navigateTo(FactsList) }
            .isEqualTo(
                UnsupportedNavigation(FactsList)
            )
    }
}

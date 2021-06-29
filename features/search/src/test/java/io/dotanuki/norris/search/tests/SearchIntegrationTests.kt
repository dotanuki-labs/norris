package io.dotanuki.norris.search.tests

import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.search.di.searchModule
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.ui.SearchActivity
import io.dotanuki.testing.app.ContainerApplication
import io.dotanuki.testing.app.setupContainerApp
import io.dotanuki.testing.rest.RestInfrastructureRule
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.direct
import org.kodein.di.instance
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(application = ContainerApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SearchIntegrationTests {

    lateinit var localStorage: LocalStorage

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    @Before fun `before each test`() {
        val app = setupContainerApp(searchModule)
        localStorage = app.di.direct.instance()
    }

    @Test fun `at first lunch, should display only suggestions`() {

        val payload =
            """
            [
                "career",
                "celebrity",
                "dev"
            ]
            """.trimIndent()

        restInfrastructure.server.enqueue(
            MockResponse().setResponseCode(200).setBody(payload)
        )

        val expectedState = SearchScreenState.Content(
            suggestions = listOf("career", "celebrity", "dev"),
            history = emptyList()
        )

        launchActivity<SearchActivity>().run {

            moveToState(Lifecycle.State.RESUMED)

            onActivity {
                Thread.sleep(1000)
                Shadows.shadowOf(Looper.getMainLooper()).idle()
                assertThat(it.actualState).isEqualTo(expectedState)
            }

            close()
        }
    }
}

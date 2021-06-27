package io.dotanuki.norri.facts

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.facts.di.factsModule
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.testing.app.ContainerApplication
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
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
class FactsScreenTests {

    lateinit var server: MockWebServer
    lateinit var localStorage: LocalStorage

    @get:Rule val executorRule = InstantTaskExecutorRule()

    @Before fun beforeEachTest() {
        server = MockWebServer().apply {
            start(port = 4242)
        }

        val app =
            InstrumentationRegistry.getInstrumentation()
                .targetContext
                .applicationContext as ContainerApplication
        val modules = app.modules
        modules += factsModule

        localStorage = app.di.direct.instance()
    }

    @After fun afterEachTest() {
        server.shutdown()
    }

    @Test fun `at first lunch, should start on empty state`() {

        launchActivity<FactsActivity>().run {
            moveToState(Lifecycle.State.RESUMED)

            onActivity {
                assertThat(it.actualState).isEqualTo(FactsScreenState.Empty)
            }

            close()
        }
    }

    @Test fun `when some search done, should display results`() {

        val payload =
            """
            {
              "total": 1,
              "result": [
                {
                  "categories": [
                    "humor"
                  ],
                  "created_at": "2016-05-01 10:51:41.584544",
                  "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                  "id": "2wzginmks8azrbaxnamxdw",
                  "updated_at": "2016-05-01 10:51:41.584544",
                  "url": "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                  "value": "Chuck Norris can divide by zero"
                }
              ]
            }
            """.trimIndent()

        server.enqueue(
            MockResponse().setResponseCode(200).setBody(payload)
        )

        localStorage.registerNewSearch("humor")

        val expectedState = FactsScreenState.Success(
            FactsPresentation(
                "humor", listOf(
                    FactDisplayRow(
                        url = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                        fact = "Chuck Norris can divide by zero",
                        displayWithSmallerFontSize = false
                    )
                )
            )
        )

        launchActivity<FactsActivity>().run {
            moveToState(Lifecycle.State.RESUMED)

            onActivity {
                Thread.sleep(1000)
                Shadows.shadowOf(Looper.getMainLooper()).idle()
                assertThat(it.actualState).isEqualTo(expectedState)
            }

            close()
        }
    }

    @Test fun `when remote service fails, should display the error`() {

        server.enqueue(
            MockResponse().setResponseCode(503)
        )

        localStorage.registerNewSearch("code")

        val expectedState = FactsScreenState.Failed(
            RemoteServiceIntegrationError.RemoteSystem
        )

        launchActivity<FactsActivity>().run {
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

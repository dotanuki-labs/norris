package io.dotanuki.norri.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.facts.di.factsModule
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.app.activityScenario
import io.dotanuki.testing.app.awaitPendingExecutions
import io.dotanuki.testing.rest.FakeChuckNorrisIO
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.direct
import org.kodein.di.instance
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class FactsIntegrationTests {

    private lateinit var localStorage: LocalStorage
    private lateinit var api: FakeChuckNorrisIO

    @Before fun `before each test`() {
        val app = TestApplication.setupWith(factsModule)

        localStorage = app.di.direct.instance()
        api = app.di.direct.instance<ChuckNorrisDotIO>() as FakeChuckNorrisIO
        api.prepare()
    }

    @Test fun `at first lunch, should start on empty state`() {
        activityScenario<FactsActivity> {
            whenResumed { target ->
                awaitPendingExecutions()
                assertThat(target.actualState).isEqualTo(FactsScreenState.Empty)
            }
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

        api.fakeSearch = payload
        localStorage.registerNewSearch("humor")

        activityScenario<FactsActivity> {
            whenResumed { target ->

                awaitPendingExecutions()

                val facts = listOf(
                    FactDisplayRow(
                        url = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                        fact = "Chuck Norris can divide by zero",
                        displayWithSmallerFontSize = false
                    )
                )
                val expectedState = FactsScreenState.Success(
                    FactsPresentation("humor", facts)
                )
                assertThat(target.actualState).isEqualTo(expectedState)
            }
        }
    }

    @Test fun `when remote service fails, should display the error`() {

        api.errorMode = true

        localStorage.registerNewSearch("code")

        activityScenario<FactsActivity> {
            whenResumed { target ->

                awaitPendingExecutions()

                val expectedState = FactsScreenState.Failed(
                    RemoteServiceIntegrationError.RemoteSystem
                )
                assertThat(target.actualState).isEqualTo(expectedState)
            }
        }
    }
}

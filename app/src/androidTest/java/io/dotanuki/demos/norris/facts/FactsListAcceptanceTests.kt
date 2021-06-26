package io.dotanuki.demos.norris.facts

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.demos.norris.dsl.ErrorImage
import io.dotanuki.demos.norris.dsl.ErrorMessage
import io.dotanuki.demos.norris.dsl.FactsContent
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN
import io.dotanuki.demos.norris.dsl.shouldBe
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.norris.facts.FactsActivity
import io.dotanuki.norris.persistance.AppPreferencesWrapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsListAcceptanceTests {

    lateinit var server: MockWebServer

    @Before fun beforeEachTest() {
        server = MockWebServer().apply {
            start(port = 4242)
        }

        resetSearches()
    }

    @After fun afterEachTest() {
        server.shutdown()
    }

    @Test fun givenFirstLaunch_shouldDisplayEmptyState() {

        scenarioLauncher<FactsActivity>().run {
            onResume {
                factsListChecks {
                    loadingIndicator shouldBe HIDDEN
                    "No facts to show" shouldBe DISPLAYED
                }
            }
        }
    }

    @Test fun givenActualSearch_shouldDisplayFacts() {
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

        setActualSearch("humor")

        scenarioLauncher<FactsActivity>().run {

            onResume {
                factsListChecks {
                    loadingIndicator shouldBe HIDDEN
                    errorState shouldBe HIDDEN
                    "Chuck Norris can divide by zero" shouldBe DISPLAYED
                }
            }
        }
    }

    @Test fun givenUnexpectedServerResponse_shouldReportProperly() {

        server.enqueue(
            MockResponse().setResponseCode(400)
        )

        setActualSearch("code")

        scenarioLauncher<FactsActivity>().run {
            onResume {
                factsListChecks {
                    loadingIndicator shouldBe HIDDEN
                    errorState shouldBe DISPLAYED
                    errorStateImage shows ErrorImage.IMAGE_BUG_FOUND
                    errorStateLabel shows ErrorMessage.MESSAGE_BUG_FOUND
                    content shouldBe FactsContent.ABSENT
                }
            }
        }
    }

    private fun resetSearches() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val prefs = AppPreferencesWrapper(app as Application).preferences
        prefs.edit().clear().commit()
    }

    private fun setActualSearch(term: String) {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val prefs = AppPreferencesWrapper(app as Application).preferences
        prefs.edit().putString("key.search.terms", term).commit()
    }
}

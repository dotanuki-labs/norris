package io.dotanuki.demos.norris.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN
import io.dotanuki.demos.norris.dsl.shouldBe
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.norris.search.SearchQueryActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchQueryAcceptanceTests {

    lateinit var server: MockWebServer

    @Before fun beforeEachTest() {
        server = MockWebServer().apply {
            start(port = 4242)
        }
    }

    @After fun afterEachTest() {
        server.shutdown()
    }

    @Test fun shouldDisplaySuggestions() {
        val payload =
            """
            [
                "career",
                "celebrity",
                "dev"
            ]
            """.trimIndent()

        server.enqueue(
            MockResponse().setResponseCode(200).setBody(payload)
        )

        scenarioLauncher<SearchQueryActivity>().run {
            onResume {
                val categories = listOf("career", "celebrity", "dev")
                val history = emptyList<String>()

                searchQueryChecks {
                    loading shouldBe HIDDEN
                    suggestions shouldDisplay categories
                    pastSearches shouldDisplay history
                }
            }
        }
    }

    @Test fun shouldReportInvalidQuery() {

        scenarioLauncher<SearchQueryActivity>().run {
            onResume {
                searchInteractions {
                    textInputField received "U2"
                }

                searchQueryChecks {
                    validationError shouldBe DISPLAYED
                    confirmQuery()
                    "Cannot proceed with an invalid query" shouldBe DISPLAYED
                }
            }
        }
    }
}

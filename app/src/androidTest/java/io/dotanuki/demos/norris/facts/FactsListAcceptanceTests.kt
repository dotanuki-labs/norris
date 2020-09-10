package io.dotanuki.demos.norris.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.demos.norris.dsl.ErrorImage.IMAGE_BUG_FOUND
import io.dotanuki.demos.norris.dsl.ErrorMessage.MESSAGE_BUG_FOUND
import io.dotanuki.demos.norris.dsl.FactsContent.ABSENT
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN
import io.dotanuki.demos.norris.dsl.shouldBe
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.norris.facts.FactsActivity
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
    }

    @After fun afterEachTest() {
        server.shutdown()
    }

    @Test fun givenSuccessfulResponse_shouldDisplayFacts() {
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

        scenarioLauncher<FactsActivity>().run {
            onResume {
                factsListChecks {
                    loadingIndicator shouldBe HIDDEN
                    errorState shouldBe DISPLAYED
                    errorStateImage shows IMAGE_BUG_FOUND
                    errorStateLabel shows MESSAGE_BUG_FOUND
                    content shouldBe ABSENT
                }
            }
        }
    }
}
package io.dotanuki.demos.norris.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import io.dotanuki.demos.norris.facts.ErrorImage.IMAGE_BUG_FOUND
import io.dotanuki.demos.norris.facts.ErrorMessage.MESSAGE_BUG_FOUND
import io.dotanuki.demos.norris.facts.FactsContent.ABSENT
import io.dotanuki.demos.norris.facts.Visibility.DISPLAYED
import io.dotanuki.demos.norris.facts.Visibility.HIDDEN
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.demos.norris.util.BindingsOverrider
import io.dotanuki.norris.domain.rest.ChuckNorrisDotIO
import io.dotanuki.norris.domain.rest.RawFact
import io.dotanuki.norris.domain.rest.RawSearch
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.UnexpectedResponse
import io.dotanuki.norris.facts.FactsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@RunWith(AndroidJUnit4::class)
class FactsListAcceptanceTests {

    @get:Rule val overrider = BindingsOverrider {
        bind<ChuckNorrisDotIO>(overrides = true) with provider {
            mockChuckNorris
        }
    }

    private val mockChuckNorris = mock<ChuckNorrisDotIO>()

    @Test fun givenSuccessfulResponse_shouldDisplayFacts() {

        val chuckNorrisFact = "Chuck Norris can divide by zero"

        val apiResponse = RawSearch(
            listOf(
                RawFact(
                    id = "987654321",
                    categories = emptyList(),
                    url = "https/api.chucknorris.io/987654321",
                    value = chuckNorrisFact
                )
            )
        )

        scenarioLauncher<FactsActivity>().run {

            beforeLaunch {
                configureService(mockChuckNorris) {
                    newSearchMatches {
                        criteria = SuccessfulSearch(apiResponse)
                    }
                }
            }

            onResume {
                assertThat {
                    loadingIndicator shouldBe HIDDEN
                    errorState shouldBe HIDDEN
                    chuckNorrisFact shouldBe DISPLAYED
                }
            }
        }
    }

    @Test fun givenUnexpectedServerResponse_shouldReportProperly() {

        scenarioLauncher<FactsActivity>().run {

            beforeLaunch {
                configureService(mockChuckNorris) {
                    newSearchMatches {
                        criteria = IssueFound(UnexpectedResponse)
                    }
                }
            }

            onResume {
                assertThat {
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
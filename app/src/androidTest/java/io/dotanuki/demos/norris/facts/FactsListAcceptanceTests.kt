package io.dotanuki.demos.norris.facts

import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import io.dotanuki.demos.norris.facts.FactsContent.absent
import io.dotanuki.demos.norris.facts.Visibility.displayed
import io.dotanuki.demos.norris.facts.Visibility.hidden
import io.dotanuki.demos.norris.util.BindingsOverrider
import io.dotanuki.norris.domain.ChuckNorrisDotIO
import io.dotanuki.norris.domain.RawFact
import io.dotanuki.norris.domain.RawSearch
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError
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

        given(mockChuckNorris) {
            responseWillMatch {
                criteria = SuccessfulSearch(apiResponse)
            }
        }

        val scenario = launchActivity<FactsActivity>().apply {
            moveToState(RESUMED)
        }

        assertThat {
            loadingIndicator shouldBe hidden
            errorState shouldBe hidden
            chuckNorrisFact shouldBe displayed
        }

        scenario.close()
    }

    @Test fun givenSomeInfrastructureError_shouldReport() {

        given(mockChuckNorris) {
            responseWillMatch {
                criteria = IssueFound(RemoteServiceIntegrationError.UnexpectedResponse)
            }
        }

        val scenario = launchActivity<FactsActivity>().apply {
            moveToState(RESUMED)
        }

        assertThat {
            loadingIndicator shouldBe hidden
            errorState shouldBe displayed
            content shouldBe absent
        }

        scenario.close()
    }
}
package io.dotanuki.demos.norris.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.demos.norris.dsl.ErrorImage.IMAGE_BUG_FOUND
import io.dotanuki.demos.norris.dsl.ErrorMessage.MESSAGE_BUG_FOUND
import io.dotanuki.demos.norris.dsl.FactsContent.ABSENT
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN
import io.dotanuki.demos.norris.dsl.shouldBe
import io.dotanuki.demos.norris.fakes.FakeApi
import io.dotanuki.demos.norris.fakes.FakeApi.Mode
import io.dotanuki.demos.norris.util.ActivityScenarioLauncher.Companion.scenarioLauncher
import io.dotanuki.demos.norris.util.BindingsOverrider
import io.dotanuki.norris.facts.FactsActivity
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@RunWith(AndroidJUnit4::class)
class FactsListAcceptanceTests {

    private val fakeApi by lazy {
        FakeApi()
    }

    @get:Rule val overrider = BindingsOverrider {
        bind<ChuckNorrisDotIO>(overrides = true) with provider {
            fakeApi
        }
    }

    @Test fun givenSuccessfulResponse_shouldDisplayFacts() {
        fakeApi.mode = Mode.SUCCESS

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
        fakeApi.mode = Mode.ERROR

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